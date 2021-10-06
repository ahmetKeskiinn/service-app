package example.com.serviceapp.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentSplashBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.admin
import example.com.serviceapp.utils.authenticationUtils.login.Authentication
import example.com.serviceapp.utils.authenticationUtils.login.AuthenticationSplash
import example.com.serviceapp.utils.authenticationUtils.login.AuthenticationStatus
import example.com.serviceapp.utils.family
import example.com.serviceapp.utils.service
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SplashFragment : Fragment(), AuthenticationSplash, Authentication, AuthenticationStatus {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var splashScreen: SplashScreenViewModel
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart() {
        initialVM()
        startAnimation()
        Handler().postDelayed({ this.goActivity() }, 500)
        super.onStart()
    }
    private fun initialVM() {
        MyApp.appComponent.inject(this)
        splashScreen = ViewModelProvider(this, viewModelFactory).get(SplashScreenViewModel::class.java)
    }

    private fun goActivity() {
        splashScreen.getInformations(this)
    }

    private fun startAnimation() {
        runBlocking {
            (
                Runnable {
                    binding.animationView.setVisibility(View.VISIBLE)
                }
                )
        }
    }

    private fun stopAnimation() {
        runBlocking {
            (
                Runnable {
                    binding.animationView.setVisibility(View.INVISIBLE)
                }
                )
        }
    }

    override fun isSuccess(data: String, boolean: Boolean) {
        if (!boolean) {
            Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_loginFragment)
        } else {
            val list = data.split(",")
            splashScreen.firebaseAuth(
                this,
                list.get(0),
                list.get(1)
            )
        }
    }

    override fun isSuccess(boolean: Boolean) {
        if (boolean) {
            splashScreen.getStatus(this)
        } else {
            Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_loginFragment)
        }
    }

    override fun getStatus(data: String) {
        if (data.equals(family)) {
            stopAnimation()
            Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_mapFragment)
        } else if (data.equals(service)) {
            stopAnimation()
            Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_mainServiceFragment)
        } else if (data.equals(admin)) {
            stopAnimation()
            Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_mainAdminFragment)
        } else {
            Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_loginFragment)
        }
    }
}
