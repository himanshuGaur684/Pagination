package gaur.himanshu.imagesearchapp.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gaur.himanshu.imagesearchapp.data.mappers.ImageDTOToImageMapper
import gaur.himanshu.imagesearchapp.data.mappers.mapAll
import gaur.himanshu.imagesearchapp.data.remote.ApiService
import gaur.himanshu.imagesearchapp.domain.model.Image

class ImagePagingSource(
    private val apiService: ApiService,
    private val q: String,
    private val imageDtoToImageMapper:ImageDTOToImageMapper
) : PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize

            val images = apiService.getImages(q = q, page = pageNumber)
            return LoadResult.Page(
                data = imageDtoToImageMapper.mapAll(images.hits),
                prevKey = if (pageNumber == 1) null else pageNumber.minus(1),
                nextKey = if (images.hits.size < pageSize) null else pageNumber.plus(1)
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}