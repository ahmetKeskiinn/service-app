package example.com.serviceapp.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentLoginBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.admin
import example.com.serviceapp.utils.family
import example.com.serviceapp.utils.service
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
        setAnimationInComponents()
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
    private fun setAnimationInComponents() {
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.loginCardView.startAnimation(animationFadeIn)
        binding.schoolBusImage.startAnimation(animationFadeIn)
    }

    private fun initialButton() {
        binding.sign.setOnClickListener {
            if (binding.rememberMe.isChecked) {
                loginViewModel.saveInfos(binding.id.text.toString(), binding.pw.text.toString())
            }
            firebaseAuth()
        }
    }
    private fun firebaseAuth() {
        loginViewModel.firebaseAuth(
            binding.id.text.toString(),
            binding.pw.text.toString()
        ).observe(
            viewLifecycleOwner,
            Observer
            {
                if (it) {
                    Log.d("TAG", "firebaseAuth: " + it)
                    getStatus()
                } else {
                    Toast.makeText(context, R.string.invalidateAuthetication, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun getStatus() {
        loginViewModel.getStatus().observe(
            viewLifecycleOwner,
            Observer
            {
                Log.d("TAG", "getStatus: " + it)
                if (it.equals(family)) {
                    Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mapFragment)
                } else if (it.equals(service)) {
                    Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mainServiceFragment)
                } else if (it.equals(admin)) {
                    Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mainAdminFragment)
                } else {
                    Toast.makeText(context, R.string.somethingWentsWrong, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
