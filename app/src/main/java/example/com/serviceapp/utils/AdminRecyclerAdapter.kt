package example.com.serviceapp.utils

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
import example.com.serviceapp.utils.authenticationUtils.admin.ClickListener

class AdminRecyclerAdapter(private val listener: ClickListener) : ListAdapter<AddChild, AdminRecyclerAdapter.ChildrenHolder>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildrenHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.admin_recycler_item,
            parent,
            false
        )
        return ChildrenHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChildrenHolder, position: Int) {
        with(getItem(position)) {
            holder.childrenName.text = this.nameSurname
            holder.childrenNumber.text = this.schoolNumber
            holder.childrenParent.text = this.parentName
        }
    }
    inner class ChildrenHolder(iv: View) : RecyclerView.ViewHolder(iv), View.OnClickListener {
        val childrenName: TextView = itemView.findViewById(R.id.childrenNameSurname)
        val childrenNumber: TextView = itemView.findViewById(R.id.childrenNumber)
        val childrenParent: TextView = itemView.findViewById(R.id.childrenParent)
        val acceptButton: ImageView = itemView.findViewById(R.id.acceptButton)
        val rejectButton: ImageView = itemView.findViewById(R.id.rejectButton)
        init {
            itemView.findViewById<ImageView>(R.id.acceptButton).setOnClickListener(this)
            itemView.findViewById<ImageView>(R.id.rejectButton).setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            if (v == acceptButton) {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    getItem(adapterPosition)?.let { it1 ->
                        listener.
                        itemAcceptClick(it1)
                    }
            }
            if (v == rejectButton) {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    getItem(adapterPosition)?.let { it1 ->
                        listener.itemDeleteClick(it1)
                    }
            }
        }
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
