package gaur.himanshu.imagesearchapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import gaur.himanshu.imagesearchapp.data.local.ImageDao
import gaur.himanshu.imagesearchapp.data.local.RemoteKeyDao
import gaur.himanshu.imagesearchapp.data.mappers.ImageDTOToImageMapper
import gaur.himanshu.imagesearchapp.data.mappers.ImageEntityToImageMapper
import gaur.himanshu.imagesearchapp.data.pagingSource.ImagePagingSource
import gaur.himanshu.imagesearchapp.data.pagingSource.ImageRemoteMediator
import gaur.himanshu.imagesearchapp.data.remote.ApiService
import gaur.himanshu.imagesearchapp.domain.model.Image
import gaur.himanshu.imagesearchapp.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImageRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: ImageDTOToImageMapper,
    private val imageDao: ImageDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val imageEntityToImageMapper: ImageEntityToImageMapper
) : ImageRepository {
    override fun getImages(q: String): Pager<Int, Image> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                ImagePagingSource(
                    apiService = apiService,
                    q = q,
                    mapper = mapper
                )
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRemoteMediatorImages(q: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = true,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                imageDao.getImages(q)
            },
            remoteMediator = ImageRemoteMediator(
                query = q,
                imageDao = imageDao,
                remoteKeyDao = remoteKeyDao,
                apiService = apiService
            )
        ).flow
            .map {
                it.map {
                    imageEntityToImageMapper.map(it)
                }
            }
    }
}