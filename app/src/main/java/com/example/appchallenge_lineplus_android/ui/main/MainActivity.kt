package com.example.appchallenge_lineplus_android.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchallenge_lineplus_android.db.memo.Memo
import com.example.appchallenge_lineplus_android.R
import com.example.appchallenge_lineplus_android.ui.main.viewmodel.MemoViewModel
import com.example.appchallenge_lineplus_android.databinding.ActivityMainBinding
import com.example.appchallenge_lineplus_android.ui.main.adapter.MemoListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener, MemoListAdapter.OnItemClickListener, TextWatcher {
    private lateinit var memoViewModel: MemoViewModel
    private lateinit var memoAdapter: MemoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val list = ArrayList<Memo>()
        memoAdapter =
            MemoListAdapter(
                list,
                this
            )

        binding.recyclerView.adapter = memoAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        memoViewModel = ViewModelProviders.of(this).get(MemoViewModel::class.java)
        memoViewModel.getListMemos().observe(this, Observer<List<Memo>> { memos -> memoAdapter.addMemos(memos!!) })

        viewInit()
    }

    private fun recyclerViewInit(){

    }

    private fun viewInit(){
        btnAdd.setOnClickListener(this)
        etSearch.addTextChangedListener(this)
    }

    override fun onClick(viewId: View?) {
        when(viewId){
            btnAdd -> memoViewModel.intentRegisterMemo()
        }
    }

    override fun onItemClick(memo: Memo) {
        memoViewModel.intentDetailMemo(memo)
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        memoAdapter.filter?.filter(p0)
    }

}
