package jp.aqua.shizen.dictionary.deck.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import jp.aqua.shizen.item.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: Item)

    @Delete
    suspend fun deleteDeck(deck: Item)

    @Update
    suspend fun updateDeck(deck: Item)

    @Query("SELECT * FROM ${Item.TABLE_NAME} ORDER BY ${Item.ID} ASC")
    fun getAllDecks(): Flow<List<Item>>

    @Query("SELECT * FROM ${Item.TABLE_NAME} WHERE ${Item.ID} = :deckId")
    fun getDeck(deckId: Int): Flow<Item>
}