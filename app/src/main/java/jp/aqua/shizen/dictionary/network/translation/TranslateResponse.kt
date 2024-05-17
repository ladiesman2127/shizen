package jp.aqua.shizen.dictionary.network.translation

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Translation(
    @Json(name = "text")
    val text: String,
    @Json(name = "detectedLanguageCode")
    val languageCode: String
)

@JsonClass(generateAdapter = true)
data class TranslateResponse(
    @Json(name = "translations")
    val translations: List<Translation>?
)