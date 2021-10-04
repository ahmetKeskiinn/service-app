package example.com.serviceapp.utils.AuthenticationUtils.Admin

import example.com.serviceapp.ui.family.feature.addChild.AddChild

interface ClickListener {
    fun itemAcceptClick(data: AddChild)
    fun itemDeleteClick(data: AddChild)
}
