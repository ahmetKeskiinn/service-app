package example.com.serviceapp.ui.teacher.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import example.com.serviceapp.databinding.FragmentMainAdminBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.utils.AdminRecyclerAdapter
import example.com.serviceapp.utils.authenticationUtils.admin.ClickListener
import example.com.serviceapp.utils.ViewModelFactory
import javax.inject.Inject

class MainAdminFragment : Fragment(), ClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainAdminViewModel: MainAdminViewModel
    private lateinit var binding: FragmentMainAdminBinding
    private lateinit var recyclerAdapter: AdminRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        initialRecycler()
        getRequestData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        mainAdminViewModel = ViewModelProvider(this, viewModelFactory).get(MainAdminViewModel::class.java)
    }
    private fun getRequestData() {
        //mainAdminViewModel.getRequestChildren(this)
        mainAdminViewModel.getRequestChildren().observe(viewLifecycleOwner, Observer {
            recyclerAdapter.submitList(it)
        })
    }
    private fun initialRecycler() {
        recyclerAdapter = AdminRecyclerAdapter(this)
        binding.addRequestRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = recyclerAdapter
        }
    }


    override fun itemAcceptClick(data: AddChild) {
        mainAdminViewModel.addChildren(data)
        getRequestData()
    }

    override fun itemDeleteClick(data: AddChild) {
        mainAdminViewModel.deleteChildren(data)
        getRequestData()
    }
}
