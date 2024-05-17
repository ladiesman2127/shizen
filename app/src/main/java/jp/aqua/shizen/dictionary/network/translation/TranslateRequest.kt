package jp.aqua.shizen.dictionary.network.translation

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TranslateRequest(
    @Json(name = "sourceLanguageCode")
    val from: String = "",
    @Json(name = "targetLanguageCode")
    val to: String,
    @Json(name = "texts")
    val texts: List<String>,
    @Json(name = "speller")
    val speller: Boolean = true
)