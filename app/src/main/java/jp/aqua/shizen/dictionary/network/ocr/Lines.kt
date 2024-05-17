package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Lines(
    @Json(name = "boundingBox")
    val boundingBox: BoundingBox? = BoundingBox(),
    @Json(name = "text")
    val text: String? = null,
    @Json(name = "words")
    val words: List<Words> = listOf(),
    @Json(name = "textSegments")
    val textSegments: List<TextSegments> = listOf(),
    @Json(name = "orientation")
    val orientation: String? = null

)