package jp.aqua.shizen.listen.video.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.model.Item.Companion.ID
import jp.aqua.shizen.item.model.Item.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Item)

    @Delete
    suspend fun deleteVideo(video: Item)

    @Update
    suspend fun updateVideo(video: Item)

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $ID ASC")
    fun getAllVideos(): Flow<List<Item>>

    @Query("SELECT * FROM $TABLE_NAME WHERE $ID = :videoId")
    fun getVideo(videoId: Int): Flow<Item>

}