package com.example.appchallenge_lineplus_android.ui.modify.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appchallenge_lineplus_android.db.memo.Memo
import com.example.appchallenge_lineplus_android.ui.modify.data.ModifyImageMemo
import java.text.SimpleDateFormat
import java.util.*

class ModifyMemoViewModel(application: Application) : AndroidViewModel(application){
    var modifyMemoId: Int = 0
    var modifyMemoWriteTime: String = ""
    lateinit var modifyImageData :MutableList<String>
    var modifyMemoTitle: String = ""
    var modifyMemoMainText: String = ""
    var modifyMemoThumnailImagePath: String = ""
    var modifyMemoImagePath: String = ""
    var modifyImageDataList = ArrayList<ModifyImageMemo>()
    var selectedPhotos = ArrayList<String>()
    var modifyViewCurrentFontSize = 12.0F

    val currentImagePath : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun setModifyCurrentImagePath(){
        currentImagePath.value = modifyMemoImagePath
    }

    fun setModifyMemo(memoId: Int, memoWriteTime:String, memoTitle: String, memoMainText: String, memoThumnailImagePath : String, memoImagePath: String){
        modifyMemoId = memoId
        modifyMemoWriteTime = memoWriteTime
        modifyMemoTitle = memoTitle
        modifyMemoMainText = memoMainText
        modifyMemoThumnailImagePath = memoThumnailImagePath

        modifyMemoImagePath = memoImagePath
        modifyImageData = stringToWords(modifyMemoImagePath)
        setRefreshImageData(modifyImageData)

        currentImagePath.value = modifyMemoImagePath
    }


    fun clearList() {
        clearImageDataList()
        clearSelectedPhots()
    }

    fun clearSelectedPhots() = selectedPhotos.clear()

    fun addImage(imageMemo: ModifyImageMemo) {
        modifyImageDataList.add(imageMemo)
    }

    fun urlImageCheck(currentUrlImage: String){
        selectedPhotos.add(currentUrlImage)
        checkThumnailImage()
    }


    fun saveMemo(memoTitle: String, memoMainText: String, dateText: String): Memo {
        val memo =
            Memo(
                modifyMemoId,
                dateText,
                memoTitle,
                memoMainText,
                modifyMemoThumnailImagePath,
                modifyMemoImagePath
            )
        return memo
    }

    private fun stringToWords(s: String) = s.trim().splitToSequence(',', ' ', '[', ']')
        .filter { it.isNotEmpty() } // or: .filter { it.isNotBlank() }
        .toMutableList()

    fun getDateText(): String {
        val currentTime = Calendar.getInstance().time
        return SimpleDateFormat("yyyy년 MM월 dd일 EE요일 HH:mm:ss", Locale.getDefault())
            .format(currentTime)
    }

    fun cameraGalleryResult(photos: Collection<String>){
        addAllSelectedPhotos(photos)
        setImagePath()
        clearImageDataList()
        finalUpdateImage()
        checkThumnailImage()
    }
    fun addAllSelectedPhotos(photos: Collection<String>) = selectedPhotos.addAll(photos)

    fun setImagePath(){
        modifyMemoImagePath = selectedPhotos.toString()
    }

    fun clearImageDataList() = modifyImageDataList.clear()

    fun checkThumnailImage(){
        modifyMemoThumnailImagePath = if (selectedPhotos.isNotEmpty()) selectedPhotos[0]
        else ""
    }

    fun finalUpdateImage(){
        val testData: List<String> = stringToWords(modifyMemoImagePath)
        for (element in testData) addImage(
            ModifyImageMemo(
                element
            )
        )
    }

    fun updateItemClick(selectedDeleteImageData: MutableList<String>){
        clearList()
        when {
            selectedDeleteImageData.isNotEmpty() -> {
                setRefreshImageData(deleteImageData(selectedDeleteImageData))
            }
        }
        setImagePath()
        checkThumnailImage()
        currentImagePath.value = modifyMemoImagePath
    }

    fun setFontSize(currentFontSize: Float){
        when(currentFontSize){
            12.0F -> modifyViewCurrentFontSize = 16.0F
            16.0F -> modifyViewCurrentFontSize = 20.0F
            20.0F -> modifyViewCurrentFontSize = 24.0F
            24.0F -> modifyViewCurrentFontSize = 12.0F
        }
    }

    fun deleteImageData(selectedDeleteImageData: MutableList<String>): MutableList<String> {
        val refreshImageData: MutableList<String> = stringToWords(modifyMemoImagePath)

        for (i in selectedDeleteImageData){
            refreshImageData.remove(i)
        }
        return refreshImageData
    }


    fun setRefreshImageData(refreshImageData: MutableList<String>){
        clearList()
        for (element in refreshImageData) {
            selectedPhotos.add(element)
            modifyImageDataList.add(
                ModifyImageMemo(
                    element
                )
            )
        }
    }
}