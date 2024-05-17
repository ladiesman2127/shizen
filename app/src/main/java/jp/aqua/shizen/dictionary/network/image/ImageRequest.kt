package jp.aqua.shizen.dictionary.network.image

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageRequest(
    @Json(name = "q")
    val q: String,
    @Json(name = "num")
    val num: Int = 5,
    @Json(name = "start")
    val start: Int = 0
)