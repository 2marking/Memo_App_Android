package com.example.appchallenge_lineplus_android.db.memo

import android.view.View
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memos")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idMemo")
    var id: Int = 0,

    @ColumnInfo(name = "writeTime")
    var writeTime: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "mainText")
    var mainText: String,

    @ColumnInfo(name = "thumnailImage")
    var thumnailImage: String,

    @ColumnInfo(name = "imagePath")
    var imagePath: String
)