package com.example.appchallenge_lineplus_android.ImagePicker.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appchallenge_lineplus_android.ImagePicker.PhotoPickerActivity
import com.example.appchallenge_lineplus_android.ImagePicker.adapter.PhotoGridAdapter
import com.example.appchallenge_lineplus_android.ImagePicker.entity.Photo
import com.example.appchallenge_lineplus_android.ImagePicker.entity.PhotoDirectory
import com.example.appchallenge_lineplus_android.ImagePicker.utils.ImageCaptureManager
import com.example.appchallenge_lineplus_android.ImagePicker.utils.MediaStoreHelper.INDEX_ALL_PHOTOS
import com.example.appchallenge_lineplus_android.ImagePicker.utils.MediaStoreHelper.PhotosResultCallback
import com.example.appchallenge_lineplus_android.ImagePicker.utils.MediaStoreHelper.getPhotoDirs
import com.example.appchallenge_lineplus_android.R
import java.io.IOException
import java.util.*


class PhotoPickerFragment : Fragment(){
    private var mContext: Context? = null
    private var mActivity: Activity? = null
    private var captureManager: ImageCaptureManager? = null
    var photoGridAdapter: PhotoGridAdapter? = null
    private var directories: MutableList<PhotoDirectory?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this.activity!!.applicationContext
        mActivity = this.activity
        directories = ArrayList()
        captureManager = ImageCaptureManager(activity!!)

        val mediaStoreArgs = Bundle()

        if (activity is PhotoPickerActivity) {
            mediaStoreArgs.putBoolean(
                PhotoPickerActivity.EXTRA_SHOW_GIF,
                (activity as PhotoPickerActivity?)!!.isShowGif
            )
        }
        getPhotoDirs(
            activity!!, mediaStoreArgs,
            object : PhotosResultCallback {
                override fun onResultCallback(dirs: List<PhotoDirectory>?) {
                    directories?.clear()
                    directories?.addAll(dirs!!)
                    photoGridAdapter!!.notifyDataSetChanged()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val rootView =
            inflater.inflate(R.layout.fragment_photo_picker, container, false)
        photoGridAdapter = PhotoGridAdapter(
            context!!,
            directories,
            (activity as PhotoPickerActivity?)!!.isCheckBoxOnly
        )


        val recyclerView = rootView.findViewById<View>(R.id.rvPhotos) as RecyclerView
        val layoutManager = StaggeredGridLayoutManager(
            (activity as PhotoPickerActivity?)!!.maxGrideItemCount,
            OrientationHelper.VERTICAL
        )
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = photoGridAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()

        photoGridAdapter!!.setOnCameraClickListener {
            try {
                val intent = captureManager!!.dispatchTakePictureIntent()
                startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return rootView
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            captureManager!!.galleryAddPic()
            if (directories!!.size > 0) {
                val path = captureManager!!.currentPhotoPath
                val directory = directories!![INDEX_ALL_PHOTOS]
                directory?.getPhotos()!!.add(
                    INDEX_ALL_PHOTOS,
                    Photo(
                        path.hashCode(),
                        path
                    )
                )
                photoGridAdapter!!.notifyDataSetChanged()
            }
        } else {
            photoGridAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        captureManager!!.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        captureManager!!.onRestoreInstanceState(savedInstanceState)
        super.onViewStateRestored(savedInstanceState)
    }
}