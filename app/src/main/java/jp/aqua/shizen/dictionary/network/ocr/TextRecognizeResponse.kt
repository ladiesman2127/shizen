package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextRecognizeResponse(
    @Json(name = "result")
    val result: TextRecognizeResult
)

@JsonClass(generateAdapter = true)
data class TextRecognizeResult(
    @Json(name = "textAnnotation")
    val textAnnotation: TextAnnotation,
)

@JsonClass(generateAdapter = true)
data class TextAnnotation(
    @Json(name = "fullText")
    val fullText: String? = null,
)

