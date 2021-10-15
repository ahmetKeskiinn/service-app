package example.com.serviceapp.utils

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import example.com.serviceapp.R


fun ImageView.updateWithUrl(url: String, imageViewAvatar: ImageView) {
    if (!url.equals("default")) {
        Log.d("TAG", "updateWithUrl: " + url)
        Picasso.get().load(url).into(imageViewAvatar)
    } else {
        Log.d("TAG", "updateWithUrl: " + url)
        Glide.with(context).load(R.drawable.ic_add_student).into(imageViewAvatar);
    }
}
