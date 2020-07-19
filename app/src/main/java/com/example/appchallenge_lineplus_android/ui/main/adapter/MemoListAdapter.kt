package com.example.appchallenge_lineplus_android.ui.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchallenge_lineplus_android.R
import com.example.appchallenge_lineplus_android.databinding.RvItemMemoBinding
import com.example.appchallenge_lineplus_android.db.memo.Memo

class MemoListAdapter(private var items: List<Memo>, listener: OnItemClickListener) :
    RecyclerView.Adapter<MemoListAdapter.ViewHolder>(), Filterable{
    private var listenerMemo: OnItemClickListener = listener
    var unFilteredMemolist: List<Memo> = emptyList()
    var filteredMemoList: List<Memo> = emptyList()

    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(memo: Memo)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item, listenerMemo)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.rv_item_memo, parent, false
            )
        )
        unFilteredMemolist = items

        return v
    }

    class ViewHolder(private val binding: RvItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Memo, listener: OnItemClickListener) {
            binding.apply {
                mainMemoItem = item
            }
            itemView.setOnClickListener{
                listener.onItemClick(item)
            }
        }
    }

    fun addMemos(memos: List<Memo>) {
        this.items = memos
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                filteredMemoList = if (charString.isEmpty()) {
                    unFilteredMemolist
                } else {
                    val filteredList: MutableList<Memo> = ArrayList()
                    for (item in unFilteredMemolist) {
                        if (item.title.toLowerCase().contains(charString.toLowerCase()) ||
                                item.mainText.toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(item)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredMemoList

                return filterResults
            }
            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                items = (filterResults.values as List<Memo>)
                notifyDataSetChanged()
            }
        }
    }
}