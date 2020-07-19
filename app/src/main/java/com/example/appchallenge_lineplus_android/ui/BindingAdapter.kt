package com.example.appchallenge_lineplus_android.ui

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.appchallenge_lineplus_android.R

@BindingAdapter("bindImageSource")
fun bindImage(view: ImageView, bindImageSource: String?){
    if (bindImageSource.isNullOrBlank()) view.visibility = INVISIBLE
    else {
        view.visibility = VISIBLE
        Glide.with(view.context)
            .load(bindImageSource)
            .error(R.drawable.ic_image_error)
            .into(view)
    }
}

@BindingAdapter("bindCurrentImage")
fun bindSelfImage(view:ImageView, bindCurrentImage: String?){
    Glide.with(view.context)
        .load(bindCurrentImage)
        .error(R.drawable.ic_image_error)
        .into(view)
}