package example.com.serviceapp.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import example.com.serviceapp.R
import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.utils.updateWithUrl

class FamilyRecylerAdapter : ListAdapter<AddChild, FamilyRecylerAdapter.ChildrenHolder>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildrenHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.family_recyclerview_item,
            parent,
            false
        )
        return ChildrenHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChildrenHolder, position: Int) {
        with(getItem(position)) {
            holder.childrenName.text = this.nameSurname
            holder.childrenNumber.text = this.schoolNumber
            holder.childrenPhoto.updateWithUrl(
                this.imageURL,
                holder.childrenPhoto
            )
        }
    }
    inner class ChildrenHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val childrenName: TextView = itemView.findViewById(R.id.childrenNameSurname)
        val childrenNumber: TextView = itemView.findViewById(R.id.childrenNumber)
        val childrenPhoto: ImageView = itemView.findViewById(R.id.childPhoto)
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<AddChild>() {
    override fun areItemsTheSame(oldItem: AddChild, newItem: AddChild): Boolean {
        return oldItem.nameSurname.equals(newItem.nameSurname)
    }

    override fun areContentsTheSame(
        oldItem: AddChild,
        newItem: AddChild
    ): Boolean {
        return oldItem.schoolNumber.equals(newItem.schoolNumber)
    }
}
