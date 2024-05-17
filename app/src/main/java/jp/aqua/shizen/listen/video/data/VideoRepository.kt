package jp.aqua.shizen.listen.video.data

import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.data.ItemRepository
import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

class VideoRepository(
    private val videoDao: VideoDao
) : ItemRepository {
    suspend fun addVideo(
        title: String,
        cover: String? = null,
    ) = videoDao.insertVideo(
        Item(
            title = title,
            cover = cover,
            creation = DateTime().toDate().time,
            language = "",
            href = ""
        )
    )

    override fun getAllItems(): Flow<List<Item>> = videoDao.getAllVideos()

    override fun getItem(id: Int): Flow<Item> = videoDao.getVideo(id)

    override suspend fun updateItem(item: Item) = videoDao.updateVideo(item)

    override suspend fun insertItem(item: Item) = videoDao.insertVideo(item)

    override suspend fun deleteItem(item: Item) = videoDao.deleteVideo(item)
}