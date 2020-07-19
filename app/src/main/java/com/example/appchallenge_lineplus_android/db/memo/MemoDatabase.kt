package com.example.appchallenge_lineplus_android.db.memo

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration


@Database(entities = [Memo::class], version = 7)
@TypeConverters(StringArrayListConverter::class)
abstract class MemoDatabase: RoomDatabase() {
    companion object {
        private var INSTANCE: MemoDatabase? = null
        fun getDatabase(context: Context): MemoDatabase {
            if (INSTANCE==null){
                INSTANCE = Room.databaseBuilder(context.applicationContext, MemoDatabase::class.java, "memo-db")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE as MemoDatabase
        }
    }
    abstract fun memoDao(): MemoDao
}
