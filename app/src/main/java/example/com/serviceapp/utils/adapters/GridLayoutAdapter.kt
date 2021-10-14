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

class GridLayoutAdapter(private val listener: LayoutClickListener) : ListAdapter<LayoutModel, GridLayoutAdapter.GirdLayoutListHolder>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GirdLayoutListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_grid_item,
            parent,
            false
        )
        return GirdLayoutListHolder(itemView)
    }

    override fun onBindViewHolder(holder: GirdLayoutListHolder, position: Int) {
        with(getItem(position)) {
            holder.bottomIcon.setBackgroundResource(this.bottomIcon)
            holder.topBackground.setBackgroundResource(this.leftXml)
            holder.title.text = this.title
            holder.subTitle.text = this.subTitle
            holder.exteriorIcon.setBackgroundResource(this.exteriorIcon)
            holder.interiorIcon.setBackgroundResource(this.interiorIcon)
            holder.midIcon.setBackgroundResource(this.midIcon)
        }
    }
    inner class GirdLayoutListHolder(iv: View) : RecyclerView.ViewHolder(iv), View.OnClickListener {
        val topBackground: ImageView = itemView.findViewById(R.id.imageView22)
        val title: TextView = itemView.findViewById(R.id.textView7)
        val subTitle: TextView = itemView.findViewById(R.id.textView6)
        val exteriorIcon: ImageView = itemView.findViewById(R.id.imageView7)
        val interiorIcon: ImageView = itemView.findViewById(R.id.imageView14)
        val bottomIcon: ImageView = itemView.findViewById(R.id.imageView24)
        val midIcon: ImageView = itemView.findViewById(R.id.icAddChildrenImage)

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
