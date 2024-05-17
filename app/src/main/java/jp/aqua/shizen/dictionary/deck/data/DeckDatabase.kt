package jp.aqua.shizen.dictionary.deck.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import jp.aqua.shizen.item.data.ItemTypeConverters
import jp.aqua.shizen.item.model.Item


@Database(
    entities = [Item::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ItemTypeConverters::class)
abstract class DeckDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao

    companion object {
        @Volatile
        var INSTANCE: DeckDatabase? = null
        fun getDatabase(context: Context): DeckDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    DeckDatabase::class.java,
                    "deck_database"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}