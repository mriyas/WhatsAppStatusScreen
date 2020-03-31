package com.riyas.whatsapp.view.utilis

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.riyas.whatsapp.R


@BindingAdapter(value = ["imageSrc", "glideListener"], requireAll = false)
fun setImageUri(view: ImageView, imageUri: String?,listener: RequestListener<Drawable>) {
loadImage(imageUri, R.drawable.ic_image_black_24dp,view,listener)
}


fun loadImage(
    url: String?,  placeHolderId: Int,
    view: ImageView,requestListner: RequestListener<Drawable>?
) {
    var mContext = view.context
    if (null == mContext)
        return
    Glide.with(mContext)
        .load(url)
        .apply(
            RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolderId)
                .dontAnimate()
        )
        .addListener(requestListner)
        .into(view)
}





