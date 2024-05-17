package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Words(

    @Json(name = "boundingBox")
    val boundingBox: BoundingBox? = BoundingBox(),
    @Json(name = "text")
    val text: String? = null,
    @Json(name = "entityIndex")
    val entityIndex: String? = null,
    @Json(name = "textSegments")
    val textSegments: List<TextSegments> = listOf()

)