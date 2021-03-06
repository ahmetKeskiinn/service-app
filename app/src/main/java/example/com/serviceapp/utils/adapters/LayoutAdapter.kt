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
import example.com.serviceapp.ui.family.feature.main.LayoutModel

class LayoutAdapter(private val listener: LayoutClickListener) : ListAdapter<LayoutModel, LayoutAdapter.LayoutListHolder>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_list_item,
            parent,
            false
        )
        return LayoutListHolder(itemView)
    }

    override fun onBindViewHolder(holder: LayoutListHolder, position: Int) {
        with(getItem(position)) {
            holder.bottomIcon.setBackgroundResource(this.bottomIcon)
            holder.leftBackground.setBackgroundResource(this.leftXml)
            holder.title.text = this.title
            holder.subTitle.text = this.subTitle
            holder.exteriorIcon.setBackgroundResource(this.exteriorIcon)
            holder.interiorIcon.setBackgroundResource(this.interiorIcon)
            holder.midIcon.setBackgroundResource(this.midIcon)
        }
    }
    inner class LayoutListHolder(iv: View) : RecyclerView.ViewHolder(iv), View.OnClickListener {
        val leftBackground: ImageView = itemView.findViewById(R.id.imageView17)
        val title: TextView = itemView.findViewById(R.id.textView8)
        val subTitle: TextView = itemView.findViewById(R.id.textView9)
        val exteriorIcon: ImageView = itemView.findViewById(R.id.imageView8)
        val interiorIcon: ImageView = itemView.findViewById(R.id.imageView12)
        val bottomIcon: ImageView = itemView.findViewById(R.id.imageView18)
        val midIcon: ImageView = itemView.findViewById(R.id.icBusImage)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            if (v == itemView) {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    getItem(adapterPosition)?.let { it1 ->
                        listener.clickedLayout(it1)
                    }
            }
        }
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<LayoutModel>() {
    override fun areItemsTheSame(oldItem: LayoutModel, newItem: LayoutModel): Boolean {
        return oldItem.subTitle.equals(newItem.subTitle)
    }

    override fun areContentsTheSame(
        oldItem: LayoutModel,
        newItem: LayoutModel
    ): Boolean {
        return oldItem.title.equals(newItem.title)
    }
}
