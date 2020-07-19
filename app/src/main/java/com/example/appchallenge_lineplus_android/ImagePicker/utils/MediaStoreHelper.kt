package com.example.appchallenge_lineplus_android.ImagePicker.utils

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore.MediaColumns
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.example.appchallenge_lineplus_android.ImagePicker.PhotoPickerActivity
import com.example.appchallenge_lineplus_android.ImagePicker.entity.PhotoDirectory
import com.example.appchallenge_lineplus_android.R
import java.util.*

object MediaStoreHelper {
    const val INDEX_ALL_PHOTOS = 0
    @JvmStatic
    fun getPhotoDirs(
        activity: FragmentActivity,
        args: Bundle?,
        resultCallback: PhotosResultCallback?
    ) {
        activity.supportLoaderManager
            .initLoader<Cursor>(
                0,
                args,
                PhotoDirLoaderCallbacks(activity, resultCallback)
            )
    }

    internal class PhotoDirLoaderCallbacks(
        private val context: Context,
        private val resultCallback: PhotosResultCallback?
    ) : LoaderManager.LoaderCallbacks<Cursor?> {
        override fun onCreateLoader(
            id: Int,
            args: Bundle?
        ): Loader<Cursor?> {
            return PhotoDirectoryLoader(
                context,
                args!!.getBoolean(PhotoPickerActivity.EXTRA_SHOW_GIF, false)
            )
        }

        override fun onLoadFinished(
            loader: Loader<Cursor?>,
            data: Cursor?
        ) {
            if (data == null) return
            val directories: MutableList<PhotoDirectory> =
                ArrayList()
            val photoDirectoryAll = PhotoDirectory()
            photoDirectoryAll.name = context.getString(R.string.customImageAll)
            photoDirectoryAll.id = "ALL"
            while (data.moveToNext()) {
                val imageId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID))
                val bucketId =
                    data.getString(data.getColumnIndexOrThrow(MediaColumns.BUCKET_ID))
                val name =
                    data.getString(data.getColumnIndexOrThrow(MediaColumns.BUCKET_DISPLAY_NAME))
                val path =
                    data.getString(data.getColumnIndexOrThrow(MediaColumns.DATA))
                val photoDirectory = PhotoDirectory()
                photoDirectory.id = bucketId
                photoDirectory.name = name
                if (!directories.contains(photoDirectory)) {
                    photoDirectory.coverPath = path
                    photoDirectory.addPhoto(imageId, path)
                    photoDirectory.dateAdded =
                        data.getLong(data.getColumnIndexOrThrow(MediaColumns.DATE_ADDED))
                    directories.add(photoDirectory)
                } else {
                    directories[directories.indexOf(photoDirectory)].addPhoto(imageId, path)
                }
                photoDirectoryAll.addPhoto(imageId, path)
            }
            if (photoDirectoryAll.photoPaths.isNotEmpty()) photoDirectoryAll.coverPath = photoDirectoryAll.photoPaths[0]

            directories.add(INDEX_ALL_PHOTOS, photoDirectoryAll)
            resultCallback?.onResultCallback(directories)
        }

        override fun onLoaderReset(loader: Loader<Cursor?>) {}

    }

    interface PhotosResultCallback {
        fun onResultCallback(directories: List<PhotoDirectory>?)
    }
}