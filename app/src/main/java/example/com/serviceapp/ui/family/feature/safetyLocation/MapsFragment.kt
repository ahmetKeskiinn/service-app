package example.com.serviceapp.ui.family.feature.safetyLocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentMapsBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.zoomCount
import javax.inject.Inject

class MapsFragment : Fragment(), OnMapReadyCallback, PermissionListener {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var selectedSafetyViewModel: SelectedSafetyLocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @Inject
    lateinit var viewmodelfactory: ViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialUI()
        initialVM()
        permissionCheck()
    }
    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }
    private fun permissionCheck() {
        if (isPermissionGiven()) {
            startMap()
        } else {
            givePermission()
        }
    }
    private fun givePermission() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this)
            .check()
    }

    private fun isPermissionGiven(): Boolean {
        return ActivityCompat.checkSelfPermission(this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun initialVM() {
        selectedSafetyViewModel = ViewModelProvider(this, viewmodelfactory).get(
            SelectedSafetyLocationViewModel::class.java
        )
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("TAG", "onMapReady: " + location?.latitude)
                Log.d("TAG", "onMapReady: " + location?.longitude)
                val coordinat = location?.latitude?.let { LatLng(it, location.longitude) }
                p0.addMarker(MarkerOptions().position(coordinat).title(getString(R.string.yourHere)))
                p0.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinat, zoomCount))
            }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        startMap()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(context, "Permission required for showing location", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        token!!.continuePermissionRequest()
    }
}
