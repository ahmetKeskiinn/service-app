package example.com.serviceapp.ui.family.feature.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentFamilyBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.utils.AdminRecyclerAdapter
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.family.ChildrenData
import example.com.serviceapp.utils.family.FamilyRecylerAdapter
import javax.inject.Inject

class FamilyFragment : Fragment(), ChildrenData {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var familyViewModel: FamilyViewModel
    private lateinit var binding: FragmentFamilyBinding
    private lateinit var recyclerAdapter: FamilyRecylerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFamilyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        initialTextViews()
        initialRecycler()
        getData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        familyViewModel = ViewModelProvider(this, viewModelFactory).get(FamilyViewModel::class.java)
    }

    private fun initialTextViews() {
        binding.addChildrenButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_addChildrenFragment)
        }
        binding.addLocationButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_selectSafetyLocationFragment)
        }
    }
    private fun initialRecycler() {
        recyclerAdapter = FamilyRecylerAdapter()
        binding.childrenRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = recyclerAdapter
        }
    }
    private fun getData(){
        familyViewModel.data.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "initialRecycler: " + it.size)
            recyclerAdapter.submitList(it)
        })
    }

    override fun childrenData(data: ArrayList<AddChild>) {
        recyclerAdapter.submitList(data)
    }
}
