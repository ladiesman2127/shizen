package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tables(

    @Json(name = "boundingBox")
    val boundingBox: BoundingBox? = BoundingBox(),
    @Json(name = "rowCount")
    val rowCount: String? = null,
    @Json(name = "columnCount")
    val columnCount: String? = null,
    @Json(name = "cells")
    val cells: List<Cells> = listOf()

)