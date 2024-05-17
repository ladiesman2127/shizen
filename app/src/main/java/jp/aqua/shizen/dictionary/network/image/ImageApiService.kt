package jp.aqua.shizen.dictionary.network.image

import jp.aqua.shizen.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val BASE_URL = "https://google-search74.p.rapidapi.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ImageApiService {
    @Headers(
        "X-RapidAPI-Key: ${BuildConfig.GOOGLE_IMAGE_API_KEY}",
        "X-RapidAPI-Host: google-search72.p.rapidapi.com"
    )
    @GET("imagesearch")
    suspend fun image(@Query("q") q: String, @Query("num") num: Int = 1): ImageResponse
}

object ImageApi {
    val retrofitService: ImageApiService by lazy {
        retrofit.create(ImageApiService::class.java)
    }
}