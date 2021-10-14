package example.com.serviceapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import example.com.serviceapp.databinding.FragmentDenemeBinding
import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.ui.family.feature.main.LayoutModel
import example.com.serviceapp.utils.adapters.ClickListener
import example.com.serviceapp.utils.adapters.LayoutAdapter
import example.com.serviceapp.utils.adapters.LayoutClickListener

class deneme : Fragment(), LayoutClickListener {

    private lateinit var binding: FragmentDenemeBinding
    private lateinit var recyclerAdapter: LayoutAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDenemeBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun content(){

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialRecycler()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun changeIconColor(icon: Int): Unit? {

        var icons = context?.let { ContextCompat.getColor(it, R.color.red) }?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(resources.getDrawable(icon)),
                it
            )
        }
        return icons
    }
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    private fun initialRecycler() {
        val list = ArrayList<LayoutModel>()
        list.add(LayoutModel(getString(R.string.where),getString(R.string.whereSub),
            R.drawable.ic_arrow_icon_family_small,
            R.drawable.bus_background_shape,
            R.drawable.ic_family_ui_icon_background,
            R.drawable.ic_family_ui_icon_background_yellow_big_small,
            R.drawable.ic_where_is_the_bus_icon_in_family))

        list.add(LayoutModel(getString(R.string.chat),getString(R.string.chatSub),
            R.drawable.ic_arrow_icon_family_small,
            R.drawable.chat_background_shape,
            R.drawable.ic_family_ui_icon_background,
            R.drawable.ic_family_ui_icon_background_purple_big_small,
            R.drawable.ic_chat_icon_in_family))
        list.add(LayoutModel(getString(R.string.addNewStudent),getString(R.string.addSafetyLcation),
            R.drawable.ic_arrow_icon_family_small,
            R.drawable.add_location_background_shape,
            R.drawable.ic_family_ui_icon_background,
            R.drawable.ic_family_ui_icon_background_blue_big_small,
            R.drawable.ic_location_icon_in_family))
        recyclerAdapter = LayoutAdapter(this)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = recyclerAdapter
        }
        recyclerAdapter.submitList(list)
    }


    override fun clickedLayout(cardView: LayoutModel) {
        Log.d("TAG", "clickedLayout: " + cardView.title)
    }

}