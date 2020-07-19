package com.example.appchallenge_lineplus_android.ui.register.viewmodel

import android.app.Application
import android.icu.text.Edits
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appchallenge_lineplus_android.db.memo.Memo
import com.example.appchallenge_lineplus_android.ui.register.data.RegisterImageMemo
import java.text.SimpleDateFormat
import java.util.*

class RegisterMemoViewModel(application: Application) : AndroidViewModel(application){
    var registerMemoId: Int = 0
    var registerMemoWriteTime: String = ""
    lateinit var registerImageData :MutableList<String>
    var registerMemoTitle: String = ""
    var registerMemoMainText: String = ""
    var registerMemoThumnailImagePath: String = ""
    var registerMemoImagePath: String = ""
    var registerImageDataList = ArrayList<RegisterImageMemo>()
    var selectedPhotos = ArrayList<String>()
    var registerViewCurrentFontSize = 12.0F


    val currentImagePath : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun setRegisterCurrentImagePath(){
        currentImagePath.value = registerMemoImagePath
    }

    fun setRegisterMemo(memoRegisterId: Int, memoWriteTime:String, memoTitle: String, memoMainText: String, memoThumnailImagePath : String, memoImagePath: String){
        registerMemoId = memoRegisterId
        registerMemoWriteTime = memoWriteTime
        registerMemoTitle = memoTitle
        registerMemoMainText = memoMainText
        registerMemoThumnailImagePath = memoThumnailImagePath

        registerMemoImagePath = memoImagePath
        registerImageData = stringToWords(registerMemoImagePath)
        setRefreshImageData(registerImageData)

        currentImagePath.value = registerMemoImagePath
    }


    fun clearList() {
        clearImageDataList()
        clearSelectedPhots()
    }

    fun clearSelectedPhots() = selectedPhotos.clear()

    fun addImage(imageMemo: RegisterImageMemo) {
        registerImageDataList.add(imageMemo)
    }

    fun urlImageCheck(currentUrlImage: String){
        selectedPhotos.add(currentUrlImage)
        checkThumnailImage()
    }

    fun saveMemo(memoTitle: String, memoMainText: String, dateText: String): Memo {
        val memo =
            Memo(
                registerMemoId,
                dateText,
                memoTitle,
                memoMainText,
                registerMemoThumnailImagePath,
                registerMemoImagePath
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
        registerMemoImagePath = selectedPhotos.toString()
    }

    fun clearImageDataList() = registerImageDataList.clear()

    fun finalUpdateImage(){
        val testData: List<String> = stringToWords(registerMemoImagePath)
        for (element in testData) addImage(
            RegisterImageMemo(
                element
            )
        )
    }

    fun checkThumnailImage(){
        registerMemoThumnailImagePath = if (selectedPhotos.isNotEmpty()) selectedPhotos[0]
        else ""
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
        currentImagePath.value = registerMemoImagePath
    }

    fun setFontSize(currentFontSize: Float){
        when(currentFontSize){
            12.0F -> registerViewCurrentFontSize = 16.0F
            16.0F -> registerViewCurrentFontSize = 20.0F
            20.0F -> registerViewCurrentFontSize = 24.0F
            24.0F -> registerViewCurrentFontSize = 12.0F
        }
    }


    fun deleteImageData(selectedDeleteImageData: MutableList<String>): MutableList<String> {
        val refreshImageData: MutableList<String> = stringToWords(registerMemoImagePath)

        for (i in selectedDeleteImageData){
            refreshImageData.remove(i)
        }
        return refreshImageData
    }

    fun setRefreshImageData(refreshImageData: MutableList<String>){
        clearList()
        for (element in refreshImageData) {
            selectedPhotos.add(element)
            registerImageDataList.add(
                RegisterImageMemo(
                    element
                )
            )
        }
    }
}