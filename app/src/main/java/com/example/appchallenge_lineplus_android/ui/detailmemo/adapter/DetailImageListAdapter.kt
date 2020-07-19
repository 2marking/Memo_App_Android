package com.example.appchallenge_lineplus_android.ui.detailmemo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchallenge_lineplus_android.R
import com.example.appchallenge_lineplus_android.databinding.RvItemDetailImageBinding
import com.example.appchallenge_lineplus_android.ui.register.viewmodel.DetailImageMemo

class DetailImageListAdapter(private var items: List<DetailImageMemo>, listener: OnItemClickListener) :
    RecyclerView.Adapter<DetailImageListAdapter.ViewHolder>() {

    private var listenerImage: OnItemClickListener = listener
    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int, detailImageMemo: DetailImageMemo)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item, listenerImage)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.rv_item_detail_image, parent, false
            )
        )
    }

    class ViewHolder(
        private val binding: RvItemDetailImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailImageMemo, clickListener: OnItemClickListener) {
            binding.apply {
                detailMemoItem = item
            }
            itemView.setOnClickListener{
                clickListener.onItemClick(itemView, adapterPosition, item)
            }
        }
    }

    fun updateImage(memos:List<DetailImageMemo>) {
        this.items = memos
        notifyDataSetChanged()
    }
}