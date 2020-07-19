package com.example.appchallenge_lineplus_android.ImagePicker.event

import android.view.View

interface OnPhotoClickListener {
    fun onClick(v: View?, position: Int, showCamera: Boolean)
}