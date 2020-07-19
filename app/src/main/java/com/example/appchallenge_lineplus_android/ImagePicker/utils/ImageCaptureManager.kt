package com.example.appchallenge_lineplus_android.ImagePicker.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.example.appchallenge_lineplus_android.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageCaptureManager(private val mContext: Context) {
    var currentPhotoPath: String? = null
        private set
    private val isNativeCamera = false
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                throw IOException()
            }
        }
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        currentPhotoPath = image.absolutePath
        return image
    }

    @Throws(IOException::class)
    fun dispatchTakePictureIntent(): Intent {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(mContext.packageManager) != null) {
            val mInfo =
                mContext.packageManager.resolveActivity(takePictureIntent, 0)
            takePictureIntent.component = ComponentName(
                mInfo.activityInfo.packageName,
                mInfo.activityInfo.name
            )
            if (isNativeCamera) {
                takePictureIntent.action = Intent.ACTION_MAIN
                takePictureIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val photoFile = createImageFile()
            if (photoFile != null) {
                val uri = FileProvider.getUriForFile(
                    mContext,
                    mContext.getString(R.string.fileProviderPath),
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
        }
        return takePictureIntent
    }

    fun galleryAddPic() {
        val dcimPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM
        ).absolutePath
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val resultImageDri = "$dcimPath/$timeStamp.jpg"
        copyFile(currentPhotoPath, resultImageDri)
        val f = File(resultImageDri)
        val mediaIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaIntent.data = Uri.fromFile(f)
        mContext.sendBroadcast(mediaIntent)
    }

    private fun copyFile(strSrc: String?, save_file: String): Boolean {
        val file = File(strSrc)
        val result: Boolean
        if (file != null && file.exists()) {
            try {
                val fis = FileInputStream(file)
                val newfos = FileOutputStream(save_file)
                var readcount = 0
                val buffer = ByteArray(1024)
                while (fis.read(buffer, 0, 1024).also { readcount = it } != -1) {
                    newfos.write(buffer, 0, readcount)
                }
                newfos.close()
                fis.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            result = true
        } else {
            result = false
        }
        return result
    }

    fun onSaveInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && currentPhotoPath != null) {
            savedInstanceState.putString(
                CAPTURED_PHOTO_PATH_KEY,
                currentPhotoPath
            )
        }
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            currentPhotoPath =
                savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY)
        }
    }

    companion object {
        private const val CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath"
        const val REQUEST_TAKE_PHOTO = 1
    }

}