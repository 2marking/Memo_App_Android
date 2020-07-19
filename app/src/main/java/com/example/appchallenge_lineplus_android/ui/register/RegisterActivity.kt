package com.example.appchallenge_lineplus_android.ui.register

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchallenge_lineplus_android.ImagePicker.PhotoPickerActivity
import com.example.appchallenge_lineplus_android.ImagePicker.utils.PhotoPickerIntent
import com.example.appchallenge_lineplus_android.R
import com.example.appchallenge_lineplus_android.databinding.ActivityRegisterBinding
import com.example.appchallenge_lineplus_android.db.memo.MemoDao
import com.example.appchallenge_lineplus_android.db.memo.MemoDatabase
import com.example.appchallenge_lineplus_android.ui.main.viewmodel.MemoViewModel
import com.example.appchallenge_lineplus_android.ui.register.adapter.RegisterImageListAdapter
import com.example.appchallenge_lineplus_android.ui.register.data.RegisterImageMemo
import com.example.appchallenge_lineplus_android.ui.register.viewmodel.RegisterMemoViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener,
    RegisterImageListAdapter.OnItemClickListener{
    private lateinit var registerMemoViewModel: RegisterMemoViewModel
    private lateinit var memoViewModel: MemoViewModel
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerImageListAdapter: RegisterImageListAdapter

    private val permissionRequestReadExternalStorage = 7
    private val permissionRequestCamera = 8
    private var memoDao: MemoDao? = null
    private var imageDeleteMode: Boolean = false
    var selectDeleteImageData: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewInit()
        setClickListener()
        initObserver()
    }

    private fun viewInit() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        memoViewModel = ViewModelProviders.of(this).get(MemoViewModel::class.java)
        registerMemoViewModel = ViewModelProviders.of(this).get(RegisterMemoViewModel::class.java)

        var db: MemoDatabase = MemoDatabase.getDatabase(this)
        memoDao = db.memoDao()

        recyclerViewInit(registerMemoViewModel.registerImageDataList)
    }

    private fun initObserver(){
        registerMemoViewModel.currentImagePath.observe(this, androidx.lifecycle.Observer<String>{
            registerImageListAdapter.updateImage(registerMemoViewModel.registerImageDataList)
        })
    }

    private fun setClickListener(){
        btnGallery.setOnClickListener(this)
        btnUrl.setOnClickListener(this)
        btnMinus.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        btnDeleteAll.setOnClickListener(this)
        btnDeleteCancel.setOnClickListener(this)
        btnDeleteComplete.setOnClickListener(this)
        btnRegisterFontSize.setOnClickListener(this)
        btnBackPress.setOnClickListener(this)
    }

    private fun recyclerViewInit(list: ArrayList<RegisterImageMemo>) {
        registerImageListAdapter =
            RegisterImageListAdapter(
                list,
                this
            )
        binding.registerImageRecyclerView.adapter = registerImageListAdapter
        binding.registerImageRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.registerImageRecyclerView.setHasFixedSize(true)
        registerImageListAdapter.updateImage(list)
    }

    override fun onClick(viewId: View?) {
        when (viewId) {
            btnBackPress -> {
                hideSoftKeyboard()
                finish()
            }
            btnGallery -> checkExternalStoragePermission()
            btnUrl -> {
                val currentUrlImage = etImageUrl.text.toString()
                if (currentUrlImage.isNullOrEmpty()) Toast.makeText(this, applicationContext.resources.getString(R.string.inputImageUrlMessage), Toast.LENGTH_SHORT).show()
                else {
                    registerMemoViewModel.urlImageCheck(currentUrlImage)
                    registerMemoViewModel.setImagePath()
                    registerMemoViewModel.addImage(
                        RegisterImageMemo(
                            currentUrlImage
                        )
                    )
                    setRegisterViewModelData()
                    Toast.makeText(this, applicationContext.resources.getString(R.string.inputImageUrlErrorMessage), Toast.LENGTH_SHORT).show()
                    etImageUrl.setText(null, TextView.BufferType.EDITABLE)

                }
            }
            btnMinus -> {
                imageDeleteMode = true
                setEditImageLayout(true)
            }
            btnRegister -> {
                val dateText = registerMemoViewModel.getDateText()
                val memoTitle = etTitle.text.toString()
                val memoMainText = etMainText.text.toString()
                when {
                    memoTitle.isEmpty() -> setToastMessage(applicationContext.resources.getString(R.string.inputTitleMessage))
                    memoMainText.isEmpty() -> setToastMessage(applicationContext.resources.getString(R.string.inputMainTextMessage))
                    else -> {
                        memoViewModel.addMemo(registerMemoViewModel.saveMemo(memoTitle, memoMainText, dateText))
                        setToastMessage(applicationContext.resources.getString(R.string.insertMemoSuccess))
                        hideSoftKeyboard()
                        finish()
                    }
                }
            }
            btnDeleteCancel -> {
                selectDeleteImageData?.clear()
                imageDeleteMode = false
                setEditImageLayout(false)
            }
            btnDeleteAll -> {
                if (imageDeleteMode){
                    selectDeleteImageData?.clear()
                    registerMemoViewModel.updateItemClick(selectDeleteImageData)
                    setToastMessage(applicationContext.resources.getString(R.string.completeMessageDeleteImageAll))
                }
                setEditImageLayout(false)
            }
            btnDeleteComplete -> {
                if (imageDeleteMode){
                    registerMemoViewModel.updateItemClick(selectDeleteImageData)
                    imageDeleteMode = false
                    selectDeleteImageData?.clear()
                    setToastMessage(applicationContext.resources.getString(R.string.completeMessageDeleteSelectImage))
                }
                setEditImageLayout(false)
            }
            btnRegisterFontSize -> {
                registerMemoViewModel.setFontSize(registerMemoViewModel.registerViewCurrentFontSize)
                etMainText.textSize =  registerMemoViewModel.registerViewCurrentFontSize
            }
        }
    }
    private fun setToastMessage(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun setEditImageLayout(currentEditImageState: Boolean){
        when(currentEditImageState){
            true -> {
                layoutImageSelectAll.visibility = GONE
                layoutImageDelete.visibility = VISIBLE
            }
            false -> {
                layoutImageSelectAll.visibility = VISIBLE
                layoutImageDelete.visibility = GONE
            }
        }
    }

    private fun setRegisterViewModelData() = registerMemoViewModel.setRegisterCurrentImagePath()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var photos: Collection<String>? = null
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS)
            }
            if (photos != null) registerMemoViewModel.cameraGalleryResult(photos)
            setRegisterViewModelData()
        }
    }

    override fun onItemClick(v:View, position:Int, registerImgaeMemo: RegisterImageMemo) {
        if (imageDeleteMode){
            val currentToggleButtonState = v.findViewById<AppCompatToggleButton>(R.id.toggleCheckImage)
            when (currentToggleButtonState.isChecked){
                true -> {
                    currentToggleButtonState.isChecked = false
                    selectDeleteImageData.remove(registerImgaeMemo.imagePath)
                }
                else -> {
                    selectDeleteImageData.add(registerImgaeMemo.imagePath)
                    currentToggleButtonState.isChecked = true
                }
            }
        }
    }

    private fun onPhotoPickerIntent(){
        val intent = PhotoPickerIntent(this)
        intent.setMaxSelectCount(20)
        intent.setShowCamera(true)
        intent.setSelectCheckBox(false)
        intent.setMaxGrideItemCount(3)
        startActivityForResult(intent, 1)
    }

    private fun checkExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permissionRequestReadExternalStorage)
            } else ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permissionRequestReadExternalStorage)
        } else checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), permissionRequestCamera)
            } else ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), permissionRequestCamera)
        } else onPhotoPickerIntent()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            permissionRequestReadExternalStorage -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) checkCameraPermission()
                else setToastMessage(applicationContext.resources.getString(R.string.requestPermissionMessageReadStorage))
                return
            }
            permissionRequestCamera -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) onPhotoPickerIntent()
                else setToastMessage(applicationContext.resources.getString(R.string.requestPermissionMessageCamera))
                return
            }
        }
    }

    private fun hideSoftKeyboard(){
        val focusedView: View? = this.currentFocus
        if (focusedView != null) {
            val imm = this.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }
}