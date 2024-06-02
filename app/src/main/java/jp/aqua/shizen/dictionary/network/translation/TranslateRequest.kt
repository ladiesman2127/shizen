package jp.aqua.shizen.dictionary.network.translation

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Генерация адаптера для конвертации
@JsonClass(generateAdapter = true)
data class TranslateRequest(
    // Язык, с которого нужно переводить
    @Json(name = "sourceLanguageCode")
    val from: String = "",
    // Язык, на который нужно переводить
    @Json(name = "targetLanguageCode")
    val to: String,
    // Список слов / предложений
    @Json(name = "texts")
    val texts: List<String>,
    // Проверка орфографии
    @Json(name = "speller")
    val speller: Boolean = true
)

