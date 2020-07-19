package com.example.appchallenge_lineplus_android.ui.main.viewmodel

import android.app.Application
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.appchallenge_lineplus_android.db.memo.Memo
import com.example.appchallenge_lineplus_android.db.memo.MemoDatabase
import com.example.appchallenge_lineplus_android.ui.detailmemo.DetailActivity
import com.example.appchallenge_lineplus_android.ui.register.RegisterActivity

class MemoViewModel(application: Application) : AndroidViewModel(application) {
    private var listMemo: LiveData<List<Memo>>
    private val context = getApplication<Application>().applicationContext
    private val appDb: MemoDatabase = MemoDatabase.getDatabase(this.getApplication())

    init {
        listMemo = appDb.memoDao().getAllContacts()
    }

    fun intentDetailMemo(memo: Memo){
        val intent = Intent(context, DetailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("idMemo", memo.id)
        context.startActivity(intent)
    }

    fun intentRegisterMemo(){
        val intent = Intent(context, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun getListMemos(): LiveData<List<Memo>> {
        return listMemo
    }

    fun addMemo(memo: Memo) {
        insertMemoAsynTask(appDb).execute(memo)
    }

    class insertMemoAsynTask(db: MemoDatabase) : AsyncTask<Memo, Void, Void>() {
        private var memoDb = db
        override fun doInBackground(vararg params: Memo): Void? {
            memoDb.memoDao().insertMemo(params[0])
            return null
        }
    }
}