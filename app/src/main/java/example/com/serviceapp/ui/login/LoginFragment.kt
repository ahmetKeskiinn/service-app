package example.com.serviceapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentLoginBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import javax.inject.Inject

class LoginFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        initialTablayout()
        initialButton()
        initialCheckBox()
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }
    private fun initialVM() {
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
    private fun initialTablayout() {
        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (binding.tabLayout.selectedTabPosition == 0) {
                        binding.id.setHint(R.string.email)
                    } else if (binding.tabLayout.selectedTabPosition == 1) {
                        binding.id.setHint(R.string.sevice)
                    } else if (binding.tabLayout.selectedTabPosition == 2) {
                        binding.id.setHint(R.string.teacherName)
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            }
        )
    }
    private fun initialButton() {
        binding.sign.setOnClickListener {
            if (binding.tabLayout.selectedTabPosition == 0) {
                Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_mapFragment)
            } else if (binding.tabLayout.selectedTabPosition == 1) {
                Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_mainServiceFragment)
            } else if (binding.tabLayout.selectedTabPosition == 2) {
                Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_mainAdminFragment)
            }
        }
    }
    private fun initialCheckBox() {
        if (binding.rememberMe.isChecked) {
        } else {
        }
    }
}
