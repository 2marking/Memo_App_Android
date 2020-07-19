package com.example.appchallenge_lineplus_android.ui.modify

import android.Manifest
import android.app.Activity
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.appchallenge_lineplus_android.ImagePicker.PhotoPickerActivity
import com.example.appchallenge_lineplus_android.ImagePicker.utils.PhotoPickerIntent
import com.example.appchallenge_lineplus_android.R
import com.example.appchallenge_lineplus_android.databinding.ActivityModifyBinding
import com.example.appchallenge_lineplus_android.db.memo.Memo
import com.example.appchallenge_lineplus_android.db.memo.MemoDao
import com.example.appchallenge_lineplus_android.db.memo.MemoDatabase
import com.example.appchallenge_lineplus_android.ui.main.viewmodel.MemoViewModel
import com.example.appchallenge_lineplus_android.ui.modify.adapter.ModifyImageListAdapter
import com.example.appchallenge_lineplus_android.ui.modify.data.ModifyImageMemo
import com.example.appchallenge_lineplus_android.ui.modify.viewmodel.ModifyMemoViewModel
import kotlinx.android.synthetic.main.activity_modify.*
import java.util.*


class ModifyActivity : AppCompatActivity(), View.OnClickListener, ModifyImageListAdapter.OnItemClickListener{
    private lateinit var memoViewModel: MemoViewModel
    private lateinit var modifyMemoViewModel: ModifyMemoViewModel
    private lateinit var binding: ActivityModifyBinding
    private lateinit var modifyImageListAdapter: ModifyImageListAdapter

    private val permissionRequestReadExternalStorage = 7
    private val permissionRequestCamera = 8
    private var memo: Memo? = null
    private var memoDao: MemoDao? = null
    private var currentMemo: Int? = null
    private var imageDeleteMode: Boolean = false
    var selectDeleteImageData: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewInit()
        setClickListener()
        initObserver()
    }


    private fun viewInit() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify)

        memoViewModel = ViewModelProviders.of(this).get(MemoViewModel::class.java)
        modifyMemoViewModel = ViewModelProviders.of(this).get(ModifyMemoViewModel::class.java)

        var db: MemoDatabase = MemoDatabase.getDatabase(this)
        memoDao = db.memoDao()

        currentMemo = intent.getIntExtra("ModifyData", -1)
        if (currentMemo != -1){
            memo = memoDao!!.getMemoById(currentMemo!!)
            modifyMemoViewModel.setModifyMemo(currentMemo!!, memo!!.writeTime, memo!!.title, memo!!.mainText, memo!!.thumnailImage, memo!!.imagePath)
            etTitle.setText(modifyMemoViewModel.modifyMemoTitle, TextView.BufferType.EDITABLE)
            etMainText.setText(modifyMemoViewModel.modifyMemoMainText, TextView.BufferType.EDITABLE)
        }
        recyclerViewInit(modifyMemoViewModel.modifyImageDataList)
    }


    private fun initObserver(){
        modifyMemoViewModel.currentImagePath.observe(this, androidx.lifecycle.Observer<String>{
            modifyImageListAdapter.updateImage(modifyMemoViewModel.modifyImageDataList)
        })
    }


    private fun setClickListener(){
        btnGallery.setOnClickListener(this)
        btnUrl.setOnClickListener(this)
        btnMinus.setOnClickListener(this)
        btnModify.setOnClickListener(this)
        btnDeleteAll.setOnClickListener(this)
        btnDeleteCancel.setOnClickListener(this)
        btnDeleteComplete.setOnClickListener(this)
        btnModifyFontSize.setOnClickListener(this)
        btnBackPress.setOnClickListener(this)
    }

    private fun recyclerViewInit(list: ArrayList<ModifyImageMemo>) {
        modifyImageListAdapter =
            ModifyImageListAdapter(
                list,
                this
            )
        binding.modifyImageRecyclerView.adapter = modifyImageListAdapter
        binding.modifyImageRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.modifyImageRecyclerView.setHasFixedSize(true)
        binding.modifyImageRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        modifyImageListAdapter.updateImage(list)
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
                    modifyMemoViewModel.urlImageCheck(currentUrlImage)
                    modifyMemoViewModel.setImagePath()
                    modifyMemoViewModel.addImage(
                        ModifyImageMemo(
                            currentUrlImage
                        )
                    )
                    setModifyViewModelData()
                    Toast.makeText(this, applicationContext.resources.getString(R.string.inputImageUrlErrorMessage), Toast.LENGTH_SHORT).show()
                    etImageUrl.setText(null, TextView.BufferType.EDITABLE)
                }
            }
            btnMinus -> {
                imageDeleteMode = true
                setEditImageLayout(true)
            }
            btnModify -> {
                val dateText = modifyMemoViewModel.getDateText()
                val memoTitle = etTitle.text.toString()
                val memoMainText = etMainText.text.toString()
                when {

                    memoTitle.isEmpty() -> setToastMessage(applicationContext.resources.getString(R.string.inputTitleMessage))
                    memoMainText.isEmpty() -> setToastMessage(applicationContext.resources.getString(R.string.inputMainTextMessage))
                    else -> {
                        memoViewModel.addMemo(modifyMemoViewModel.saveMemo(memoTitle, memoMainText, dateText))
                        setToastMessage(applicationContext.resources.getString(R.string.modifyMemoSuccess))
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
                    modifyMemoViewModel.updateItemClick(selectDeleteImageData)
                    setToastMessage(applicationContext.resources.getString(R.string.completeMessageDeleteImageAll))
                }
                setEditImageLayout(false)
            }
            btnDeleteComplete -> {
                if (imageDeleteMode){
                    modifyMemoViewModel.updateItemClick(selectDeleteImageData)
                    imageDeleteMode = false
                    selectDeleteImageData?.clear()
                    setToastMessage(applicationContext.resources.getString(R.string.completeMessageDeleteSelectImage))
                }
                setEditImageLayout(false)
            }
            btnModifyFontSize -> {
                modifyMemoViewModel.setFontSize(modifyMemoViewModel.modifyViewCurrentFontSize)
                etMainText.textSize =  modifyMemoViewModel.modifyViewCurrentFontSize
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

    private fun setModifyViewModelData() = modifyMemoViewModel.setModifyCurrentImagePath()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var photos: Collection<String>? = null
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS)
            if (photos != null) modifyMemoViewModel.cameraGalleryResult(photos)
            setModifyViewModelData()
        }
    }

    override fun onItemClick(v:View, position:Int, modifyImageMemo: ModifyImageMemo) {
        if (imageDeleteMode) {
            val test = v.findViewById<AppCompatToggleButton>(R.id.toggleCheckImage)
            if (test.isChecked) {
                test.isChecked = false
                selectDeleteImageData.remove(modifyImageMemo.imagePath)
            } else {
                selectDeleteImageData.add(modifyImageMemo.imagePath)
                test.isChecked = true
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


