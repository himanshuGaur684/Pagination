package gaur.himanshu.imagesearchapp.data.mappers

import gaur.himanshu.imagesearchapp.data.model.local.ImageEntity
import gaur.himanshu.imagesearchapp.data.model.remote.ImageDTO

class ImageDTOToImageEntityMapper(
    private val query: String
) : Mapper<ImageDTO, ImageEntity> {
    override fun map(from: ImageDTO): ImageEntity {
        return ImageEntity(
            id = from.id.toString(),
            imageUrl = from.largeImageURL,
            query = query
        )
    }
}