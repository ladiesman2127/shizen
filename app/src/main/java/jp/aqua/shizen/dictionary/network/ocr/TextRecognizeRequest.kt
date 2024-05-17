package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextRecognizeRequest(
    @Json(name = "mimeType")
    val mimeType: String,
    @Json(name = "languageCodes")
    val languageCodes: List<String> = listOf("*"),
    @Json(name = "model")
    val model: String = "",
    @Json(name = "content")
    val content: String
)