package jp.aqua.shizen.dictionary.network.translation

import jp.aqua.shizen.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://translate.api.cloud.yandex.net/translate/v2/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface TranslateApiService {
    @Headers("Authorization: Api-Key ${BuildConfig.YANDEX_TRANSLATE_API_KEY}")
    @POST("translate")
    suspend fun translate(@Body translateRequest: TranslateRequest): TranslateResponse
}

object TranslateApi {
    val retrofitService: TranslateApiService by lazy {
        retrofit.create(TranslateApiService::class.java)
    }
}