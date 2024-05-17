package jp.aqua.shizen.dictionary.network.image

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "title")
    val title: String,
    @Json(name = "thumbnailImageUrl")
    val thumbnailImageUrl: String,
    @Json(name = "originalImageUrl")
    val originalImageUrl: String,
    @Json(name = "contextLink")
    val contextLink: String,
    @Json(name = "height")
    val height: Int,
    @Json(name = "width")
    val width: Int,
    @Json(name = "size")
    val size: String
)
@JsonClass(generateAdapter = true)
data class ImageResponse(
    @Json(name = "status")
    val status: String?,
    @Json(name = "items")
    val items: List<Image>?
)