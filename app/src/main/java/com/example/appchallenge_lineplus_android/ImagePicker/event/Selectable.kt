package com.example.appchallenge_lineplus_android.ImagePicker.event

import com.example.appchallenge_lineplus_android.ImagePicker.entity.Photo

interface Selectable {
    fun isSelected(photo: Photo?): Boolean
    fun toggleSelection(photo: Photo?)
    val selectedItemCount: Int
    val selectedPhotos: MutableList<Photo>?

}