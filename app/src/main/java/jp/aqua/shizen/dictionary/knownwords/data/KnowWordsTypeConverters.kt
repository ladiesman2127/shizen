package jp.aqua.shizen.dictionary.knownwords.data

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class KnowWordsTypeConverters {
    private val _moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @OptIn(ExperimentalStdlibApi::class)
    private val _adapter = _moshi.adapter<Map<String, Int>>()

    @TypeConverter
    @ToJson
    fun fromList(list: Map<String, Int>): String {
        return _adapter.toJson(list)
    }

    @TypeConverter
    @FromJson
    fun toList(data: String): Map<String, Int>? {
        return _adapter.fromJson(data)
    }
}