package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Entities(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "text")
    val text: String? = null
)