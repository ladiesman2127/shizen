package jp.aqua.shizen.dictionary.knownwords.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import jp.aqua.shizen.dictionary.knownwords.model.KnownWords
import jp.aqua.shizen.dictionary.knownwords.model.KnownWords.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow


@Dao
interface KnownWordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(knownWords: KnownWords)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getKnownWords(): Flow<List<KnownWords>>

    @Update
    suspend fun updateKnownWords(knownWords: KnownWords)
}