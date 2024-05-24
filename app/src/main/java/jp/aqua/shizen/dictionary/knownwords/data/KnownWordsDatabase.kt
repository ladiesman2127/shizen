package jp.aqua.shizen.dictionary.knownwords.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.aqua.shizen.dictionary.knownwords.model.KnownWords

@Database(
    entities = [KnownWords::class],
    version = 1
)
@TypeConverters(KnowWordsTypeConverters::class)
abstract class KnownWordsDatabase : RoomDatabase() {

    abstract fun knownWordsDao(): KnownWordsDao

    companion object {
        @Volatile
        private var INSTANCE: KnownWordsDatabase? = null

        fun getDatabase(context: Context): KnownWordsDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, KnownWordsDatabase::class.java, "known_words_db")
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}