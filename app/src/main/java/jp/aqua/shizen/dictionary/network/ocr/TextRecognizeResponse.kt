package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextRecognizeResult(
    @Json(name = "textAnnotation")
    val textAnnotation: TextAnnotation,
    @Json(name = "page")
    val page: String? = null
)


@JsonClass(generateAdapter = true)
data class TextRecognizeResponse(
    @Json(name = "result")
    val result: TextRecognizeResult
)