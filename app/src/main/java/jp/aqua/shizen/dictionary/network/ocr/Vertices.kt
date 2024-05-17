package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Vertices(

    @Json(name = "x")
    val x: String? = null,
    @Json(name = "y")
    val y: String? = null

)