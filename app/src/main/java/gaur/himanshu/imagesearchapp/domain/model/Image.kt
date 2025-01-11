package gaur.himanshu.imagesearchapp.domain.model

import java.util.UUID

data class Image(
    val id: String,
    val imageUrl: String,
    val uuid: String = UUID.randomUUID().toString()
)
