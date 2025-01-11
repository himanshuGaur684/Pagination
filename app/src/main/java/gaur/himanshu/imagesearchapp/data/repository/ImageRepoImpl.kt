package gaur.himanshu.imagesearchapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import gaur.himanshu.imagesearchapp.data.mappers.ImageDTOToImageMapper
import gaur.himanshu.imagesearchapp.data.pagingSource.ImagePagingSource
import gaur.himanshu.imagesearchapp.data.remote.ApiService
import gaur.himanshu.imagesearchapp.domain.model.Image
import gaur.himanshu.imagesearchapp.domain.repository.ImageRepository

class ImageRepoImpl(
    private val apiService: ApiService,
    private val mapper: ImageDTOToImageMapper
) : ImageRepository {
    override  fun getImages(q: String): Pager<Int, Image> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = false,
                initialLoadSize = 10,
            ),
            pagingSourceFactory = {
                ImagePagingSource(
                    apiService = apiService, imageDtoToImageMapper = mapper,
                    q = q
                )
            }
        )

    }
}