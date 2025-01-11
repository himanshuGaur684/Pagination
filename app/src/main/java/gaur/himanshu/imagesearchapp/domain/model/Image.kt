package gaur.himanshu.imagesearchapp.domain.model

import java.util.UUID

data class Image(
    val uuid:String = UUID.randomUUID().toString(),
    val id: String,
    val imageUrl: String
)
