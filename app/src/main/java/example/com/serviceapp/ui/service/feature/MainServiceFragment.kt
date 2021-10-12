package example.com.serviceapp.ui.service.feature

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import example.com.serviceapp.databinding.FragmentMainServiceBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.adapters.ServiceAdapter
import example.com.serviceapp.utils.services.BackgroundService
import javax.inject.Inject

class MainServiceFragment : Fragment(), PermissionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var androidService: BackgroundService
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
        permissionCheck()
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

    private fun permissionCheck() {
        if (isPermissionGiven()) {
            startService()
        } else {
            givePermission()
        }
    }
    private fun givePermission() {
        Dexter.withActivity(activity)
            .withPermission(ACCESS_FINE_LOCATION)
            .withListener(this)
            .check()
    }

    private fun isPermissionGiven(): Boolean {
        return ActivityCompat.checkSelfPermission(this.requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun startService() {
        val intent = Intent(this.context?.applicationContext, androidService::class.java)
        requireActivity().startService(intent)
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        startService()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(context, "Permission required for showing location", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        token!!.continuePermissionRequest()
    }
}
