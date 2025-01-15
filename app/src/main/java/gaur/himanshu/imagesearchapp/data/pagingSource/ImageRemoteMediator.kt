package gaur.himanshu.imagesearchapp.data.pagingSource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gaur.himanshu.imagesearchapp.data.local.ImageDao
import gaur.himanshu.imagesearchapp.data.local.RemoteKeyDao
import gaur.himanshu.imagesearchapp.data.mappers.ImageDTOToImageEntityMapper
import gaur.himanshu.imagesearchapp.data.mappers.mapAll
import gaur.himanshu.imagesearchapp.data.model.local.ImageEntity
import gaur.himanshu.imagesearchapp.data.model.local.RemoteKey
import gaur.himanshu.imagesearchapp.data.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val query: String,
    private val imageDao: ImageDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val apiService: ApiService
) : RemoteMediator<Int, ImageEntity>() {
    override suspend fun initialize(): InitializeAction {
        val hasLocalData = imageDao.getCountCorrespondingToQuery(query) > 0
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
        val mapper = ImageDTOToImageEntityMapper(query)
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = state.anchorPosition?.let {
                    state.closestItemToPosition(it)?.let {
                        remoteKeyDao.getRemoteKey(it.id)
                    }
                }
                remoteKey?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKey = state.pages?.firstOrNull {
                    it.data.isNotEmpty()
                }?.data?.firstOrNull()?.let {
                    remoteKeyDao.getRemoteKey(it.id)
                }
                remoteKey?.prevKey ?: return MediatorResult.Success(remoteKey != null)
            }

            LoadType.APPEND -> {
                val remoteKey = state.pages?.lastOrNull {
                    it.data.isNotEmpty()
                }?.data?.lastOrNull()?.let {
                    remoteKeyDao.getRemoteKey(it.id)
                }
                remoteKey?.nextKey ?: return MediatorResult.Success(remoteKey != null)
            }
        }

        if (imageDao.getCountCorrespondingToQuery(query) > page.times(state.config.pageSize)) {
            return MediatorResult.Success(false)
        }

        return try {
            val response = apiService.getImages(q = query, page = page)
            val remoteImages = response.hits.distinctBy { it.id }
            val endOfPaginationReached = remoteImages.size < state.config.pageSize
            val prevPage = if (page > 1) page - 1 else null
            val nextPage = if (endOfPaginationReached) null else page + 1

            val remoteKeys = remoteImages.map {
                RemoteKey(
                    id = it.id.toString(),
                    prevKey = prevPage,
                    nextKey = nextPage,
                    query = query
                )
            }
            remoteKeyDao.insertAll(remoteKeys)
            imageDao.insertAll(mapper.mapAll(remoteImages))
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}