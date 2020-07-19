package com.example.appchallenge_lineplus_android.ui.modify.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchallenge_lineplus_android.R
import com.example.appchallenge_lineplus_android.databinding.RvItemImageModifyBinding
import com.example.appchallenge_lineplus_android.ui.modify.data.ModifyImageMemo

class ModifyImageListAdapter(private var items: List<ModifyImageMemo>, listener: OnItemClickListener) :
    RecyclerView.Adapter<ModifyImageListAdapter.ViewHolder>() {
    private var clickListenerImageMemo: OnItemClickListener = listener

    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int, imageMemo: ModifyImageMemo)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item, clickListenerImageMemo)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val view =
            ViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.rv_item_image_modify,
                    parent,
                    false
                )
            )
        return view
    }

    class ViewHolder(
        private val binding: RvItemImageModifyBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ModifyImageMemo, clickListener: OnItemClickListener) {
            binding.apply {
                modifyMemoItem = item
            }
            binding.toggleCheckImage.isChecked = false
            itemView.setOnClickListener{
                clickListener.onItemClick(itemView, adapterPosition, item)
            }
        }
    }

    fun updateImage(memos:List<ModifyImageMemo>) {
        this.items = memos
        notifyDataSetChanged()
    }
}