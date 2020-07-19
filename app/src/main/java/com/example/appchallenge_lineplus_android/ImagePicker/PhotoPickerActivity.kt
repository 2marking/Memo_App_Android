package com.example.appchallenge_lineplus_android.ImagePicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.appchallenge_lineplus_android.ImagePicker.PhotoPickerActivity
import com.example.appchallenge_lineplus_android.ImagePicker.entity.Photo
import com.example.appchallenge_lineplus_android.ImagePicker.event.OnItemCheckListener
import com.example.appchallenge_lineplus_android.ImagePicker.fragment.PhotoPickerFragment
import com.example.appchallenge_lineplus_android.R

class PhotoPickerActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 7
    private val MY_PERMISSIONS_REQUEST_CAMERA = 8
    private var pickerFragment: PhotoPickerFragment? = null
    private var showCamera = true
    private var menuDoneItem: MenuItem? = null
    private var maxCount = DEFAULT_MAX_COUNT
    @JvmField
    var maxGrideItemCount = DEFAULT_MAX_GRIDE_ITEM_COUNT
    @JvmField
    var isCheckBoxOnly = false

    private var menuIsInflated = false
    var isShowGif = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_picker)
        checkExternalStoragePermission()
    }

    private fun checkExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        } else {
            checkCameraPermission()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
            } else ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
        } else init()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) checkCameraPermission()
                else setToastMessage(applicationContext.resources.getString(R.string.requestPermissionMessageReadStorage))
                return
            }
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) init()
                else setToastMessage(applicationContext.resources.getString(R.string.requestPermissionMessageCamera))
                return
            }
        }
    }

    private fun init() {
        showCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true)
        isShowGif = intent.getBooleanExtra(EXTRA_SHOW_GIF, false)
        isCheckBoxOnly = intent.getBooleanExtra(EXTRA_CHECK_BOX_ONLY, false)
        maxGrideItemCount = intent.getIntExtra(EXTRA_MAX_GRIDE_ITEM_COUNT, DEFAULT_MAX_GRIDE_ITEM_COUNT)
        isShowGif = isShowGif
        val mToolbar =
            findViewById<View>(R.id.toolbar) as Toolbar
        mToolbar.setTitle(R.string.customImageSelect)
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.elevation = 25f
        }
        maxCount = intent.getIntExtra(
            EXTRA_MAX_COUNT,
            DEFAULT_MAX_COUNT
        )
        setPickerFragment()
    }

    private fun setPickerFragment() {
        if (pickerFragment == null) {
            pickerFragment = supportFragmentManager.findFragmentById(R.id.photoPickerFragment) as PhotoPickerFragment?
            pickerFragment!!.photoGridAdapter?.setShowCamera(showCamera)
            pickerFragment!!.photoGridAdapter?.setOnItemCheckListener(object : OnItemCheckListener {
                override fun onItemCheck(
                    position: Int,
                    path: Photo?,
                    isCheck: Boolean,
                    selectedItemCount: Int
                ): Boolean {
                    val total = selectedItemCount + if (isCheck) -1 else 1
                    menuDoneItem!!.isEnabled = total > 0
                    if (maxCount <= 1) {
                        var photos = pickerFragment!!.photoGridAdapter?.selectedPhotos
                        if (!photos?.contains(path)!!) {
                            photos.clear()
                            pickerFragment!!.photoGridAdapter?.notifyDataSetChanged()
                        }
                        return true
                    }
                    if (total > maxCount) {
                        Toast.makeText(
                            activity,
                            getString(R.string.customImageSelectMaxCount, maxCount),
                            Toast.LENGTH_LONG
                        ).show()
                        return false
                    }
                    menuDoneItem!!.title = getString(
                        R.string.customImageSelectCount,
                        total,
                        maxCount
                    )
                    return true
                }
            })
        } else {
            pickerFragment!!.photoGridAdapter?.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setToastMessage(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!menuIsInflated) {
            menuInflater.inflate(R.menu.menu_picker, menu)
            menuDoneItem = menu.findItem(R.id.done)
            menuIsInflated = true
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        if (item.itemId == R.id.done) {
            val intent = Intent()
            val selectedPhotos =
                pickerFragment!!.photoGridAdapter?.selectedPhotoPaths
            intent.putStringArrayListExtra(
                KEY_SELECTED_PHOTOS,
                selectedPhotos
            )
            setResult(Activity.RESULT_OK, intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    val activity: PhotoPickerActivity
        get() = this

    companion object {
        const val EXTRA_MAX_COUNT = "MAX_COUNT"
        const val EXTRA_SHOW_CAMERA = "SHOW_CAMERA"
        const val EXTRA_SHOW_GIF = "SHOW_GIF"
        const val EXTRA_CHECK_BOX_ONLY = "CHECK_BOX_ONLY"
        const val KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS"
        const val EXTRA_MAX_GRIDE_ITEM_COUNT = "MAX_GRIDE_IMAGE_COUNT"
        const val DEFAULT_MAX_COUNT = 9
        const val DEFAULT_MAX_GRIDE_ITEM_COUNT = 3
    }
}