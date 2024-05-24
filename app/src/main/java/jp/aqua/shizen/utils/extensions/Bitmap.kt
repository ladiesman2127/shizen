package jp.aqua.shizen.utils.extensions

import java.io.ByteArrayOutputStream
import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import jp.aqua.shizen.dictionary.network.ocr.OcrApi
import jp.aqua.shizen.dictionary.network.ocr.TextRecognizeRequest
import org.apache.commons.codec.binary.Base64

// Добавление расширения для класса Bitmap
@OptIn(UnstableApi::class)
suspend fun Bitmap?.getText(): String {
    // Возврат пустой строки, если экземпляр Bitmap равен нулю
    if (this == null) return ""
    // Создание выходной байтового потока
    val byteArrayOutputStream = ByteArrayOutputStream()
    // Компрессия в PNG с сохранением качества на уровне 100%
    compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    // Конвертация в байтовый массив
    val byteArray = byteArrayOutputStream.toByteArray()
    // Ковертация в Base64
    val content = Base64.encodeBase64String(byteArray)
    // Создание запроса к API
    val request = TextRecognizeRequest(mimeType = MimeTypes.IMAGE_PNG, content = content)
    // Получение ответа
    val response = OcrApi.retrofitService.recognizeText(request)
    // Возврат распознанного текста
    return response.result.textAnnotation.fullText ?: ""
}

