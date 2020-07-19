package com.example.appchallenge_lineplus_android.ui.detailmemo.imageviewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.appchallenge_lineplus_android.R


class DetailImageFragment : Fragment() {
    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_detail_image, container, false)

        val tvImageIndex: TextView = view.findViewById(R.id.tvImageIndex)
        val ivImageIndex: ImageView = view.findViewById(R.id.ivDetailImage)

        if (arguments != null) {
            val args = arguments
            val currentIndex = args!!.getInt("currentImageIndex")
            val imageSize = args!!.getInt("ImageSize")

            tvImageIndex.text = "$currentIndex / $imageSize"
            Glide.with(this)
                .load(args!!.getString("imgRes"))
                .error(R.drawable.ic_image_error)
                .into(ivImageIndex)
        }

        return view
    }
}