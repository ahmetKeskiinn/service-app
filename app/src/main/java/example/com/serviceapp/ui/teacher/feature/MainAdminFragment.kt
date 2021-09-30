package example.com.serviceapp.ui.teacher.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import example.com.serviceapp.databinding.FragmentMainAdminBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import javax.inject.Inject

class MainAdminFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainAdminViewModel: MainAdminViewModel
    private lateinit var binding: FragmentMainAdminBinding
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
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }
    private fun initialVM() {
        mainAdminViewModel = ViewModelProvider(this, viewModelFactory).get(MainAdminViewModel::class.java)
    }
}
