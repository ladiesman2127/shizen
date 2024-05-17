package jp.aqua.shizen.listen.video.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.aqua.shizen.item.data.ItemTypeConverters
import jp.aqua.shizen.item.model.Item

@Database(
    entities = [Item::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ItemTypeConverters::class)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao

    companion object {
        @Volatile
        private var INSTANCE: VideoDatabase? = null

        fun getDatabase(context: Context): VideoDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    VideoDatabase::class.java,
                    "video_database"
                )
            }
                .build()
                .also { INSTANCE = it }
        }
    }
}