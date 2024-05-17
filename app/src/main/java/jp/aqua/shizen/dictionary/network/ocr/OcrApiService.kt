package jp.aqua.shizen.dictionary.network.ocr

import jp.aqua.shizen.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://ocr.api.cloud.yandex.net/ocr/v1/"

val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface OcrApiService {
    @Headers("Authorization: Api-Key ${BuildConfig.YANDEX_OCR_API_KEY}")
    @POST("recognizeText")
    suspend fun recognizeText(@Body recognizeRequest: TextRecognizeRequest): TextRecognizeResponse
}

object OcrApi {
    val retrofitService by lazy {
        retrofit.create(OcrApiService::class.java)
    }
}