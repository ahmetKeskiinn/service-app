package example.com.serviceapp.utils

import android.graphics.Color
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import example.com.serviceapp.R

fun ImageView.updateWithUrl(url: String, imageViewAvatar: ImageView) {
    if (!url.equals(default)) {
        val transformation: Transformation = RoundedTransformationBuilder()
            .borderColor(Color.BLACK)
            .borderWidthDp(imageBorderWidth)
            .cornerRadiusDp(imageBorderRadius)
            .oval(false)
            .build()



        Picasso.get()
            .load(url)
            .fit()
            .transform(transformation)
            .into(imageViewAvatar)
    } else {
        Glide.with(context).load(R.drawable.ic_add_student).into(imageViewAvatar)
    }
}
