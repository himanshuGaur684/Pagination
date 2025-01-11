package gaur.himanshu.imagesearchapp.data.remote

import gaur.himanshu.imagesearchapp.data.model.remote.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // https://pixabay.com/api/?key=40308333-07c19e899666cb68334ed3a46&q=yellow+flowers&image_type=photo&pretty=true

    @GET("api/")
    suspend fun getImages(
        @Query("key") apiKey: String = "40308333-07c19e899666cb68334ed3a46",
        @Query("q") q: String,
        @Query("page") page: Int
    ): ImageResponse

}