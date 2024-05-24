package jp.aqua.shizen.item.data

import jp.aqua.shizen.item.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getAllItems(): Flow<List<Item>>

    fun getItem(id: Int): Flow<Item>

    suspend fun deleteItem(item: Item)

    suspend fun insertItem(item: Item)

    suspend fun updateItem(item: Item)
}

