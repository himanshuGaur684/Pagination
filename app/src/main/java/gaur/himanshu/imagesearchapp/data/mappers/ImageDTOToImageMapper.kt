package gaur.himanshu.imagesearchapp.data.mappers

import gaur.himanshu.imagesearchapp.data.model.remote.ImageDTO
import gaur.himanshu.imagesearchapp.domain.model.Image
import javax.inject.Inject

class ImageDTOToImageMapper @Inject constructor() : Mapper<ImageDTO, Image> {

    override fun map(from: ImageDTO): Image {
        return Image(
            id = from.id.toString(),
            imageUrl = from.largeImageURL
        )
    }
}