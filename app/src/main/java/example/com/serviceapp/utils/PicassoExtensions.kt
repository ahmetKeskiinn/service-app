package example.com.serviceapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import example.com.serviceapp.R

fun ImageView.updateWithUrl(url: String, imageViewAvatar: ImageView) {
    if (!url.equals(default)) {
        Picasso.get().load(url).resize(targetWidth, targetHeight).into(imageViewAvatar)
    } else {
        Glide.with(context).load(R.drawable.ic_add_student).into(imageViewAvatar)
    }
}
