package gaur.himanshu.imagesearchapp.data.remote

import gaur.himanshu.imagesearchapp.data.model.remote.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// https://pixabay.com/api/?key=40308333-07c19e899666cb68334ed3a46&q=yellow+flowers&page=1

interface ApiService {

    @GET("api/")
    suspend fun getImages(
        @Query("key") apiKey: String = "40308333-07c19e899666cb68334ed3a46",
        @Query("q") q: String,
        @Query("page") page: Int
    ): ImageResponse

}