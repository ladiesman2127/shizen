package jp.aqua.shizen.item.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Item.TABLE_NAME)
data class Item(
    // Аннотация для определения первичного ключа с автоматической генерацией
    @PrimaryKey(autoGenerate = true)
    // Определение название слобца в базе данных
    @ColumnInfo(name = ID)
    val id: Int? = null,
    @ColumnInfo(name = TITLE)
    // Название книги/папки/доски
    val title: String,
    @ColumnInfo(name = COVER)
    // Путь к обложке, которая сохраняется локально
    val cover: String? = null,
    @ColumnInfo(name = HREF)
    // Путь к основному файлу, если такой имеется
    val href: String? = null,
    @ColumnInfo(name = AUTHOR)
    // Список авторов
    val authors: List<String>? = null,
    @ColumnInfo(name = LANGUAGE)
    // Язык материала
    val language: String = "EN",
    @ColumnInfo(name = CREATION_DATE)
    // Дата добавления/создания
    val creation: Long,
    @ColumnInfo(name = PROGRESSION)
    // Отображение прогресса, необходимо только при работе с книгами
    val progression: String = "{}",
    @ColumnInfo(name = STATUS)
    // Статус (не начат, начат, закончен)
    val status: Status = Status.NotStarted,
    @ColumnInfo(name = DESCRIPTION)
    // Описание
    val description: String? = null,
    @ColumnInfo(name = TABLE_OF_CONTENTS)
    // Содержание, список глав книги / видео / аудио / карточек слов
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

