package com.example.appchallenge_lineplus_android.ImagePicker.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import com.example.appchallenge_lineplus_android.ImagePicker.PhotoPickerActivity

class PhotoPickerIntent : Intent, Parcelable {
    constructor(packageContext: Context?) : super(
        packageContext,
        PhotoPickerActivity::class.java
    )

    fun setMaxSelectCount(photoCount: Int) {
        this.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, photoCount)
    }

    fun setShowCamera(showCamera: Boolean) {
        this.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera)
    }

    fun setMaxGrideItemCount(grideCount: Int) {
        this.putExtra(PhotoPickerActivity.EXTRA_MAX_GRIDE_ITEM_COUNT, grideCount)
    }

    fun setSelectCheckBox(isCheckbox: Boolean) {
        this.putExtra(PhotoPickerActivity.EXTRA_CHECK_BOX_ONLY, isCheckbox)
    }
}