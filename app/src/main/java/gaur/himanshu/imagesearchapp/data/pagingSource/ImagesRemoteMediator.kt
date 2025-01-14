package gaur.himanshu.imagesearchapp.data.pagingSource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import gaur.himanshu.imagesearchapp.AppDatabase
import gaur.himanshu.imagesearchapp.data.mappers.ImageDTOToImageEntity
import gaur.himanshu.imagesearchapp.data.mappers.mapAll
import gaur.himanshu.imagesearchapp.data.model.local.ImageEntity
import gaur.himanshu.imagesearchapp.data.model.local.RemoteKey
import gaur.himanshu.imagesearchapp.data.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class ImagesRemoteMediator(
    private val query: String,
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
) : RemoteMediator<Int, ImageEntity>() {
    
    override suspend fun initialize(): InitializeAction {
        val hasLocalData = appDatabase.getImageDao().countBasedOnQuery(query) > 0
        return if (hasLocalData) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {
        val mapper = ImageDTOToImageEntity(query)
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = state.anchorPosition?.let { position ->
                    state.closestItemToPosition(anchorPosition = position)?.imageUrl?.let {
                        appDatabase.getRemoteKeyDao().getRemoteKeys(it)
                    }
                }
                remoteKey?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKey = state.pages.firstOrNull {
                    it.data.isNotEmpty()
                }?.data?.firstOrNull()?.let { image ->
                    appDatabase.withTransaction {
                        appDatabase.getRemoteKeyDao().getRemoteKeys(image.imageUrl)
                    }
                }
                remoteKey?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)

            }

            LoadType.APPEND -> {
                val remoteKey = state.pages.lastOrNull {
                    it.data.isNotEmpty()
                }?.data?.lastOrNull()?.let { image ->
                    appDatabase.withTransaction {
                        appDatabase.getRemoteKeyDao().getRemoteKeys(image.id)
                    }
                }
                remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
        }

        if ((appDatabase.getImageDao()
                .countBasedOnQuery(query) - page * state.config.pageSize) > state.config.pageSize && loadType != LoadType.REFRESH
        ) {
            return MediatorResult.Success(false)
        }

        return try {
            val response = apiService.getImages(q = query, page = page)
            val remoteImages = response.hits.distinctBy { it.id }
            val endOfPaginationReached = remoteImages.size < state.config.pageSize
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (endOfPaginationReached) null else page + 1

            val remoteKeys = remoteImages.map {
                RemoteKey(
                    imageId = it.id.toString(),
                    prevKey = prevKey,
                    nextKey = nextKey,
                    query = query
                )
            }

            appDatabase.withTransaction {
                appDatabase.getRemoteKeyDao().insertAll(remoteKeys)
            }

            val imageEntities = mapper.mapAll(remoteImages)

            appDatabase.withTransaction {
                appDatabase.getImageDao().insertAll(imageEntities)
            }

            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}