package jp.aqua.shizen.item.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = Item.TABLE_NAME)
    data class Item(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        val id: Int? = null,
        @ColumnInfo(name = TITLE)
        val title: String,
        @ColumnInfo(name = COVER)
        val cover: String? = null,
        @ColumnInfo(name = HREF)
        val href: String,
        @ColumnInfo(name = AUTHOR)
        val authors: List<String>? = null,
        @ColumnInfo(name = LANGUAGE)
        val language: String = "EN",
        @ColumnInfo(name = CREATION_DATE)
        val creation: Long,
        @ColumnInfo(name= PROGRESSION)
        val progression: String = "{}",
        @ColumnInfo(name = STATUS)
        val status: Status = Status.NotStarted,
        @ColumnInfo(name = DESCRIPTION)
        val description: String? = null,
        @ColumnInfo(name = TABLE_OF_CONTENTS)
        val tableOfContents: List<TocEntry> = emptyList()
    ) {

    fun doesMatchSearchQuery(query: String): Boolean {
        return title.lowercase().contains(query.lowercase())
    }

    companion object {
        const val TABLE_NAME = "items"
        const val ID = "id"
        const val HREF = "href"
        const val TITLE = "title"
        const val COVER = "cover"
        const val AUTHOR = "author"
        const val LANGUAGE = "language"
        const val CREATION_DATE = "creation_date"
        const val PROGRESSION = "progression"
        const val STATUS = "status"
        const val DESCRIPTION = "description"
        const val TABLE_OF_CONTENTS = "table_of_contents"
    }
}