package jp.aqua.shizen.item.data

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.aqua.shizen.item.model.TocEntry

class ItemTypeConverters {
    private val _moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @OptIn(ExperimentalStdlibApi::class)
    private val _listAdapter = _moshi.adapter<List<String>>()

    @TypeConverter
    @ToJson
    fun fromList(list: List<String>): String {
        return _listAdapter.toJson(list)
    }

    @TypeConverter
    @FromJson
    fun toList(data: String): List<String>? {
        return _listAdapter.fromJson(data)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val _tocEntryAdapter = _moshi.adapter<TocEntry>()

    @TypeConverter
    @ToJson
    fun fromToEntry(tocEntry: TocEntry): String {
        return _tocEntryAdapter.toJson(tocEntry)
    }

    @TypeConverter
    @FromJson
    fun toTocEntry(data: String): TocEntry? {
        return _tocEntryAdapter.fromJson(data)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val _tocAdapter = _moshi.adapter<List<TocEntry>>()

    @TypeConverter
    @ToJson
    fun fromToc(toc: List<TocEntry>): String {
        return _tocAdapter.toJson(toc)
    }

    @TypeConverter
    @FromJson
    fun toToc(data: String): List<TocEntry>? {
        return _tocAdapter.fromJson(data)
    }

}