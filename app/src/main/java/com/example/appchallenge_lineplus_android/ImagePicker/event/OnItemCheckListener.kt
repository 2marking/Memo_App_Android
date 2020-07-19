package com.example.appchallenge_lineplus_android.ImagePicker.event

import com.example.appchallenge_lineplus_android.ImagePicker.entity.Photo

interface OnItemCheckListener {
    fun onItemCheck(
        position: Int,
        path: Photo?,
        isCheck: Boolean,
        selectedItemCount: Int
    ): Boolean
}
