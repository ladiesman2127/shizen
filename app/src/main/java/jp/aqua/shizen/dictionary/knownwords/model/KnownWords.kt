package jp.aqua.shizen.dictionary.knownwords.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.aqua.shizen.dictionary.knownwords.model.KnownWords.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class KnownWords(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int? = null,
    @ColumnInfo(name = WORDS)
    val words: Map<String, Int>
) {
    companion object {
        const val ID = "id"
        const val WORDS = "words"
        const val TABLE_NAME = "known_words"
    }
}

