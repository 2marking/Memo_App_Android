package com.example.appchallenge_lineplus_android.db.memo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appchallenge_lineplus_android.db.memo.Memo

@Dao
interface MemoDao {
    @Query("select * from memos ORDER BY writeTime DESC")
    fun getAllContacts(): LiveData<List<Memo>>

    @Query("select * from memos where idMemo in (:id)")
    fun getMemoById(id: Int): Memo

    @Query("select * from memos where idMemo in (:id)")
    fun getCurrentMemoById(id: Int): LiveData<Memo>

    @Query("delete from memos")
    fun deleteAllMemos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: Memo)

    @Update
    fun updateMemo(memo: Memo)

    @Delete
    fun deleteMemo(memo: Memo)

    @Query("SELECT imagePath FROM memos where idMemo in (:id)")
    fun getImageAll(id: Int): String

}