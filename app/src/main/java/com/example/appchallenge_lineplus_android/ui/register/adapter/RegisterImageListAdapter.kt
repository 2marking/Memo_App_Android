package com.example.appchallenge_lineplus_android.ui.register.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchallenge_lineplus_android.R
import com.example.appchallenge_lineplus_android.databinding.RvItemImageBinding
import com.example.appchallenge_lineplus_android.ui.register.data.RegisterImageMemo

class RegisterImageListAdapter(private var items: List<RegisterImageMemo>, listener: OnItemClickListener) :
    RecyclerView.Adapter<RegisterImageListAdapter.ViewHolder>() {

    private var clickListenerImageMemo: OnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int, imageMemo: RegisterImageMemo)
    }

    override fun getItemCount(): Int = items.size

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
                    R.layout.rv_item_image, parent,
                    false
                )
            )
        return view
    }

    class ViewHolder(
        private val binding: RvItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RegisterImageMemo, clickListener: OnItemClickListener) {
            binding.apply {
                registerImageMemo = item
            }
            binding.toggleCheckImage.isChecked = false
            itemView.setOnClickListener{
                clickListener.onItemClick(itemView, adapterPosition, item)
            }
        }
    }

    fun updateImage(memos:List<RegisterImageMemo>) {
        this.items = memos
        notifyDataSetChanged()
    }
}