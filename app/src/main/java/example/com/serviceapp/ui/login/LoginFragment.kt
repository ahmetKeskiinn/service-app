package example.com.serviceapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentLoginBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.authenticationUtils.Authentication
import example.com.serviceapp.utils.authenticationUtils.AuthenticationStatus
import javax.inject.Inject

class LoginFragment : Fragment(), Authentication, AuthenticationStatus {
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
        initialButton()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    private fun initialButton() {
        binding.sign.setOnClickListener {
            if (binding.rememberMe.isChecked) {
                loginViewModel.saveInfos(binding.id.text.toString(), binding.pw.text.toString())
            }
            loginViewModel.firebaseAuth(this, binding.id.text.toString(), binding.pw.text.toString())
        }
    }

    override fun isSuccess(boolean: Boolean) {
        if (boolean) {
            loginViewModel.getStatus(this)
        } else {
            Toast.makeText(context, R.string.invalidateAuthetication, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getStatus(data: String) {
        if (data.equals("family")) {
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mapFragment)
        } else if (data.equals("service")) {
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mainServiceFragment)
        } else if (data.equals("admin")) {
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mainAdminFragment)
        } else {
            Toast.makeText(context, R.string.somethingWentsWrong, Toast.LENGTH_SHORT).show()
        }
    }
}
