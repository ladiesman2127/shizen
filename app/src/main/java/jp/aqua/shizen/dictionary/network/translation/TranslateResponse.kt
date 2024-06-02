package jp.aqua.shizen.dictionary.network.translation

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TranslateResponse(
    // Список переводов
    @Json(name = "translations")
    val translations: List<Translation>?
)

@JsonClass(generateAdapter = true)
data class Translation(
    // Переведенный текст
    @Json(name = "text")
    val text: String,
    // Обнаруженный язык
    @Json(name = "detectedLanguageCode")
    val languageCode: String
)


