package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Cells(

    @Json(name = "boundingBox")
    val boundingBox: BoundingBox? = BoundingBox(),
    @Json(name = "rowIndex")
    val rowIndex: String? = null,
    @Json(name = "columnIndex")
    val columnIndex: String? = null,
    @Json(name = "columnSpan")
    val columnSpan: String? = null,
    @Json(name = "rowSpan")
    val rowSpan: String? = null,
    @Json(name = "text")
    val text: String? = null,
    @Json(name = "textSegments")
    val textSegments: List<TextSegments> = listOf()

)