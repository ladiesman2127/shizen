package jp.aqua.shizen.read.book.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.item.model.Item.Companion.TABLE_NAME
import jp.aqua.shizen.item.model.Item.Companion.ID
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    /**
     * Inserts a book
     * @param book The book to insert
     * @return ID of the book that was inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Item)

    /**
     * Deletes a book
     * @param book The book to delete
     */
    @Delete
    suspend fun deleteBook(book: Item)

    @Update
    suspend fun updateBook(book: Item)

    /**
     * Retrieve a book from its ID.
     * @param bookId The id of the book
     */
    @Query("SELECT * FROM $TABLE_NAME WHERE $ID = :bookId")
    fun getBook(bookId: Int): Flow<Item>

    @Query("SELECT * FROM $TABLE_NAME WHERE $ID = :bookId")
    fun getBookImmediately(bookId: Int): Item

    /**
     * Retrieve all the books
     */
    @Query("SELECT * FROM $TABLE_NAME ORDER BY $ID ASC ")
    fun getAllBooks(): Flow<List<Item>>
}