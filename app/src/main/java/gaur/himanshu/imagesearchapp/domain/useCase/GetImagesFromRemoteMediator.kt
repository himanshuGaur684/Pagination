package gaur.himanshu.imagesearchapp.domain.useCase

import gaur.himanshu.imagesearchapp.domain.repository.ImageRepository
import javax.inject.Inject

class GetImagesFromRemoteMediator @Inject constructor(
    private val imageRepository: ImageRepository
) {

    operator fun invoke(q:String) = imageRepository.getRemoteMediatorImages(q)
}