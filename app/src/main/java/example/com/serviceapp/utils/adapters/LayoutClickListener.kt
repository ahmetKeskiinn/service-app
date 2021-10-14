package example.com.serviceapp.utils.adapters

import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.ui.family.feature.main.LayoutModel


interface LayoutClickListener {
    fun clickedLayout(cardView: LayoutModel)
}