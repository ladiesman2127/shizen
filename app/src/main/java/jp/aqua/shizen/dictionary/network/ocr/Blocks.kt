package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Blocks(
    @Json(name = "boundingBox")
    val boundingBox: BoundingBox? = BoundingBox(),
    @Json(name = "lines")
    val lines: List<Lines> = listOf(),
    @Json(name = "languages")
    val languages: List<Languages> = listOf(),
    @Json(name = "textSegments")
    val textSegments: List<TextSegments> = listOf()

)