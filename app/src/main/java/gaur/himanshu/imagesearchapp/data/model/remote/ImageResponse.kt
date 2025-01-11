package gaur.himanshu.imagesearchapp.data.model.remote

data class ImageResponse(
    val hits: List<ImageDTO>,
    val total: Int,
    val totalHits: Int
)