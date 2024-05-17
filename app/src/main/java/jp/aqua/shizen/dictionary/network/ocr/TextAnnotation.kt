package jp.aqua.shizen.dictionary.network.ocr

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextAnnotation(
    @Json(name = "width")
    val width: String? = null,
    @Json(name = "height")
    val height: String? = null,
    @Json(name = "blocks")
    val blocks: List<Blocks> = listOf(),
    @Json(name = "entities")
    val entities: List<Entities> = listOf(),
    @Json(name = "tables")
    val tables: List<Tables> = listOf(),
    @Json(name = "fullText")
    val fullText: String? = null,
    @Json(name = "rotate")
    val rotate: String? = null

)