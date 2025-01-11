package gaur.himanshu.imagesearchapp.domain.useCase

import gaur.himanshu.imagesearchapp.domain.repository.ImageRepository
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(private val repository: ImageRepository) {

    operator fun invoke(q: String) = repository.getImages(q)

}