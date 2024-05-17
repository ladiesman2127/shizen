package jp.aqua.shizen.utils.extensions

import java.io.ByteArrayOutputStream
import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import jp.aqua.shizen.dictionary.network.ocr.OcrApi
import jp.aqua.shizen.dictionary.network.ocr.TextRecognizeRequest
import org.apache.commons.codec.binary.Base64

@OptIn(UnstableApi::class)
suspend fun Bitmap?.getText(): String {
    if (this == null) return ""
    val byteArrayOutputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    val content = Base64.encodeBase64String(byteArray)
    val request = TextRecognizeRequest(mimeType = MimeTypes.IMAGE_PNG, content = content)
    val response = OcrApi.retrofitService.recognizeText(request)
    return response.result.textAnnotation.fullText ?: ""
}