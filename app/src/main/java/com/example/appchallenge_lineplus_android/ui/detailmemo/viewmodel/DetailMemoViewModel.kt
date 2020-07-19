package com.example.appchallenge_lineplus_android.ui.detailmemo.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appchallenge_lineplus_android.db.memo.Memo
import com.example.appchallenge_lineplus_android.db.memo.MemoDatabase
import com.example.appchallenge_lineplus_android.ui.detailmemo.imageviewpager.DetailMemoImageActivity
import com.example.appchallenge_lineplus_android.ui.modify.ModifyActivity
import com.example.appchallenge_lineplus_android.ui.register.viewmodel.DetailImageMemo

class DetailMemoViewModel(application: Application) : AndroidViewModel(application) {
    private val appDb: MemoDatabase = MemoDatabase.getDatabase(this.getApplication())
    private val context = getApplication<Application>().applicationContext
    var detailMemoId: Int = 0
    lateinit var detailImageData :List<String>
    lateinit var detailMemoTitle: String
    lateinit var detailMemoMainText: String
    lateinit var detailMemoThumnailImagePath: String
    lateinit var detailMemoImagePath: String
    var detailViewCurrentFontSize = 12.0F
    private var listMemo: LiveData<Memo>


    var detailImageDataList = ArrayList<DetailImageMemo>()

    init {
        listMemo = appDb.memoDao().getCurrentMemoById(detailMemoId)
    }

    val currentMemo : MutableLiveData<Memo> by lazy {
        MutableLiveData<Memo>()
    }

    fun setDeleteMemo(memo: Memo){
        appDb.memoDao().deleteMemo(memo!!)
    }

    fun createViewPager(prevPos: Int, currentDetailImagePath: String){
        val intent = Intent(context, DetailMemoImageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("currentPostion", prevPos)
        intent.putExtra("detailImagePath", currentDetailImagePath)
        context.startActivity(intent)
    }

    fun intentMemoModify(){
        val intent = Intent(context, ModifyActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("ModifyData", detailMemoId)
        context.startActivity(intent)
    }

    fun setMemoId(memoId: Int){
        detailMemoId = memoId
    }

    fun setDetailMemo(memoId: Int, memoWriteTime: String, memoTitle: String, memoMainText: String, memoThumnailImagePath : String, memoImagePath: String){
        detailMemoId = memoId
        detailMemoTitle = memoTitle
        detailMemoMainText = memoMainText
        detailMemoThumnailImagePath = memoThumnailImagePath

        changeImagePathType(memoImagePath)
        currentMemo.value = Memo(detailMemoId, memoWriteTime, memoTitle, memoMainText, memoThumnailImagePath, memoImagePath)
    }

    fun changeImagePathType(memoImagePath: String){
        detailMemoImagePath = memoImagePath
        detailImageData = stringToWords(detailMemoImagePath)
        detailImageDataList.clear()
        for (element in detailImageData) detailImageDataList.add(DetailImageMemo(element))
    }

    fun setFontSize(currentFontSize: Float){
        when(currentFontSize){
            12.0F -> detailViewCurrentFontSize = 16.0F
            16.0F -> detailViewCurrentFontSize = 20.0F
            20.0F -> detailViewCurrentFontSize = 24.0F
            24.0F -> detailViewCurrentFontSize = 12.0F
        }
    }


    private fun stringToWords(s: String) = s.trim().splitToSequence(',', ' ', '[', ']')
        .filter { it.isNotEmpty() } // or: .filter { it.isNotBlank() }
        .toMutableList()
}