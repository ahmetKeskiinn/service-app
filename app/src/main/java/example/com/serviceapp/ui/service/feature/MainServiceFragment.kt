package example.com.serviceapp.ui.service.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import example.com.serviceapp.databinding.FragmentMainServiceBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.adapters.ServiceAdapter
import javax.inject.Inject

class MainServiceFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainServiceViewModel: MainServiceViewModel
    private lateinit var binding: FragmentMainServiceBinding
    private lateinit var recyclerAdapter: ServiceAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        initialRecycler()
        getData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        mainServiceViewModel = ViewModelProvider(this, viewModelFactory).get(MainServiceViewModel::class.java)
    }
    private fun initialRecycler() {
        recyclerAdapter = ServiceAdapter()
        binding.studentList.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = recyclerAdapter
        }
    }
    private fun getData() {
        mainServiceViewModel.getChildrenData().observe(
            viewLifecycleOwner,
            Observer {
                recyclerAdapter.submitList(it)
            }
        )
    }
}
