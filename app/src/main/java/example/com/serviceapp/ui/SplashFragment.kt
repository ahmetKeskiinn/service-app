package example.com.serviceapp.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentSplashBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.admin
import example.com.serviceapp.utils.family
import example.com.serviceapp.utils.service
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SplashFragment : Fragment() {
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
        splashScreen.getInformations().observe(
            viewLifecycleOwner,
            Observer
            {
                if (!it.isEmpty()) {
                    auth(it)
                } else {
                    Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_loginFragment)
                }
            }
        )
    }
    private fun auth(infos: String) {
        val list = infos.split(",")
        splashScreen.firebaseAuth(
            list.get(0),
            list.get(1)
        ).observe(
            viewLifecycleOwner,
            Observer
            {
                if (it) {
                    getStatus()
                } else {
                    Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_loginFragment)
                }
            }
        )
    }

    private fun getStatus() {
        splashScreen.getStatus().observe(
            viewLifecycleOwner,
            Observer
            {
                if (it.equals(family)) {
                    stopAnimation()
                    Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_mapFragment)
                } else if (it.equals(service)) {
                    stopAnimation()
                    Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_mainServiceFragment)
                } else if (it.equals(admin)) {
                    stopAnimation()
                    Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_mainAdminFragment)
                } else {
                    Navigation.findNavController(binding.root).navigate(R.id.action_splashFragment2_to_loginFragment)
                }
            }
        )
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
}
