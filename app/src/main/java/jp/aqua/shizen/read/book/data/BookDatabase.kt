package jp.aqua.shizen.read.book.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.aqua.shizen.item.data.ItemTypeConverters
import jp.aqua.shizen.item.model.Item

/**
 * Database for storing books
 */

@Database(
    entities = [Item::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ItemTypeConverters::class)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null
        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    BookDatabase::class.java,
                    "books_database"
                )
            }
                .build()
                .also { INSTANCE = it }
        }
    }
}
