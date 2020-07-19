package com.example.appchallenge_lineplus_android.ui.detailmemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.appchallenge_lineplus_android.ImagePicker.utils.PhotoPickerIntent
import com.example.appchallenge_lineplus_android.R
import com.example.appchallenge_lineplus_android.databinding.ActivityDetailmemoBinding
import com.example.appchallenge_lineplus_android.db.memo.Memo
import com.example.appchallenge_lineplus_android.db.memo.MemoDao
import com.example.appchallenge_lineplus_android.db.memo.MemoDatabase
import com.example.appchallenge_lineplus_android.ui.detailmemo.adapter.DetailImageListAdapter
import com.example.appchallenge_lineplus_android.ui.detailmemo.viewmodel.DetailMemoViewModel
import com.example.appchallenge_lineplus_android.ui.register.viewmodel.DetailImageMemo
import kotlinx.android.synthetic.main.activity_detailmemo.*
import java.util.*


class DetailActivity : AppCompatActivity(), View.OnClickListener,
    DetailImageListAdapter.OnItemClickListener {
    lateinit var binding: ActivityDetailmemoBinding
    private var currentMemo: Int? = null
    private var memo: Memo? = null
    private var memoDao: MemoDao? = null

    private lateinit var viewModel: DetailMemoViewModel
    private lateinit var detailMemoAdapter: DetailImageListAdapter

    private val permissionRequestReadExternalStorage = 7
    private val permissionRequestCamera = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewInit()
        initExtraData()
        initClickListener()
        initObserver()
    }

    private fun viewInit() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailmemo)
        binding.tvMemoMainText.movementMethod = ScrollingMovementMethod()
    }

    private fun initExtraData(){
        var db: MemoDatabase = MemoDatabase.getDatabase(this)
        memoDao = db.memoDao()

        viewModel = ViewModelProviders.of(this).get(DetailMemoViewModel::class.java)

        currentMemo = intent.getIntExtra("idMemo", -1)
        viewModel.setMemoId(currentMemo!!)
        if (currentMemo != -1){
            memo = memoDao!!.getMemoById(currentMemo!!)
            viewModel.setDetailMemo(currentMemo!!, memo!!.writeTime, memo!!.title, memo!!.mainText, memo!!.thumnailImage, memo!!.imagePath)
        }
        recyclerViewInit(viewModel.detailImageDataList)
    }

    private fun initClickListener(){
        btnModify.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnDetailFontSize.setOnClickListener(this)
    }

    private fun initObserver(){
        viewModel.currentMemo.observe(this, androidx.lifecycle.Observer<Memo> {
            tvMemoTitle.text = it.title
            tvMemoMainText.text = it.mainText
            detailMemoAdapter.updateImage(viewModel.detailImageDataList)
        })
    }

    override fun onResume() {
        super.onResume()
        memo = memoDao!!.getMemoById(currentMemo!!)
        viewModel.setDetailMemo(currentMemo!!, memo!!.writeTime, memo!!.title, memo!!.mainText, memo!!.thumnailImage, memo!!.imagePath)
    }

    private fun recyclerViewInit(list: ArrayList<DetailImageMemo>) {
        detailMemoAdapter =
            DetailImageListAdapter(
                list,
                this
            )

        binding.detailImageRecyclerView.adapter = detailMemoAdapter
        binding.detailImageRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.detailImageRecyclerView.setHasFixedSize(true)

        detailMemoAdapter.updateImage(list)
    }

    private fun setToastMessage(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun onClick(viewId: View?) {
        when (viewId) {
            btnModify -> checkExternalStoragePermission()
            btnDelete -> {
                viewModel.setDeleteMemo(memo!!)
                setToastMessage(applicationContext.resources.getString(R.string.deleteMemoSuccess))
                finish()
            }
            btnDetailFontSize -> {
                viewModel.setFontSize(viewModel.detailViewCurrentFontSize)
                tvMemoMainText.textSize =  viewModel.detailViewCurrentFontSize
            }
        }
    }

    override fun onItemClick(v: View, position: Int, detailImageMemo: DetailImageMemo) = viewModel.createViewPager(position, viewModel.detailMemoImagePath)

    private fun checkExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permissionRequestReadExternalStorage)
            } else ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permissionRequestReadExternalStorage)
        } else viewModel.intentMemoModify()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            permissionRequestReadExternalStorage -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) viewModel.intentMemoModify()
                else Toast.makeText(this,"읽기 퍼미션이 필요합니다", Toast.LENGTH_LONG).show()
                return
            }
        }
    }
}


