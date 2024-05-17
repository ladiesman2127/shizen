package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextSegments(

    @Json(name = "startIndex")
    val startIndex: String? = null,
    @Json(name = "length")
    val length: String? = null

)