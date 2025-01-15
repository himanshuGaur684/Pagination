package gaur.himanshu.imagesearchapp.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import gaur.himanshu.imagesearchapp.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getImages(q: String): Pager<Int, Image>

    fun getRemoteMediatorImages(q: String): Flow<PagingData<Image>>

}