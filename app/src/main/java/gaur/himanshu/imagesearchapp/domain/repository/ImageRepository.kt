package gaur.himanshu.imagesearchapp.domain.repository

import androidx.paging.Pager
import gaur.himanshu.imagesearchapp.domain.model.Image

interface ImageRepository {

    fun getImages(q: String): Pager<Int, Image>

}