package com.example.appchallenge_lineplus_android.ImagePicker.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import androidx.loader.content.CursorLoader

class PhotoDirectoryLoader : CursorLoader {
    private val IMAGE_PROJECTION = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED
    )

    constructor(context: Context?, showGif: Boolean) : super(context!!) {
        projection = IMAGE_PROJECTION
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"
        selection = MediaColumns.MIME_TYPE + "=? or " + MediaColumns.MIME_TYPE + "=? " + if (showGif) "or " + MediaColumns.MIME_TYPE + "=?" else ""
        val selectionArgs: Array<String> = when(showGif) {
            true -> arrayOf("image/jpeg", "image/png", "image/gif")
            else -> arrayOf("image/jpeg", "image/png")
        }
        setSelectionArgs(selectionArgs)
    }

    private constructor(
        context: Context,
        uri: Uri,
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String
    ) : super(context, uri, projection, selection, selectionArgs, sortOrder) {
    }
}