package jp.aqua.shizen.dictionary.network.translation

import jp.aqua.shizen.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Базовая ссылка к API
private const val BASE_URL = "https://translate.api.cloud.yandex.net/translate/v2/"

// Экземпляр класс Retrofit
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create()) // Добавление конвертера Moshi
    .baseUrl(BASE_URL) // Установка базовой ссылки
    .build()

// Интерфейс для работы с API
interface TranslateApiService {
    // Установка заголовка с ключом к API
    @Headers("Authorization: Api-Key ${BuildConfig.YANDEX_TRANSLATE_API_KEY}")
    // Опредение методы
    @POST("translate")
    suspend fun translate(@Body translateRequest: TranslateRequest): TranslateResponse
}


// Объект для поддержки паттерна программирования Singleton
object TranslateApi {
    // Ленивая реализация интерфейса для работы с API
    val retrofitService: TranslateApiService by lazy {
        retrofit.create(TranslateApiService::class.java)
    }
}

