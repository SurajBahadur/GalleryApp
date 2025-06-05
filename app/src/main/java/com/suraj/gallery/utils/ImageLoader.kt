package com.suraj.gallery.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gallery.R

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
object ImageLoader {

    fun loadImage(imageView: ImageView, uri: String) {
        Glide.with(imageView.context)
            .load(uri)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(imageView)
    }

    fun loadThumbnail(imageView: ImageView, uri: String) {
        Glide.with(imageView.context)
            .load(uri)
            .override(200, 200)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(imageView)
    }
}