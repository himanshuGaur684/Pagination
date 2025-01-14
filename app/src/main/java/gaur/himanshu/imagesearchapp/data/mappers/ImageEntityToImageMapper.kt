package gaur.himanshu.imagesearchapp.data.mappers

import gaur.himanshu.imagesearchapp.data.model.local.ImageEntity
import gaur.himanshu.imagesearchapp.domain.model.Image
import java.util.UUID
import javax.inject.Inject

class ImageEntityToImageMapper @Inject constructor() : Mapper<ImageEntity, Image> {
    override fun map(from: ImageEntity): Image {
        return Image(
            id = from.id,
            imageUrl = from.imageUrl,
            uuid = UUID.randomUUID().toString()
        )
    }
}