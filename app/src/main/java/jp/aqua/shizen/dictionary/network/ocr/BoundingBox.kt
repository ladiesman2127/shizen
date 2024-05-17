package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)

data class BoundingBox(

    @Json(name = "vertices")
    val vertices: List<Vertices> = listOf()

)