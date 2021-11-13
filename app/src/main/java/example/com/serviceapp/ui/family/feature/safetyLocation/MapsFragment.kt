package example.com.serviceapp.ui.family.feature.safetyLocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
    private var markerOptions: MarkerOptions? = null
    private var locationList = ArrayList<SafetyLocationModel>()
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
        onBackPressed()
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
        return ActivityCompat.checkSelfPermission(
            this.requireContext(),
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

    override fun onMapReady(p0: GoogleMap) {
        var locationCoordinat: LatLng? = null
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("TAG", "onMapReady: " + location?.latitude)
                if(location !=null){
                    val coordinat = location?.latitude?.let { LatLng(it, location.longitude) }
                    locationCoordinat = coordinat
                    p0.addMarker(
                        MarkerOptions().position(coordinat).title(getString(R.string.yourHere))
                    )
                    p0.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinat, zoomCount))
                }

            }
        addFirebaseLocations(p0)
        p0.setOnMapClickListener(
            OnMapClickListener { point ->
                if (markerOptions != null) {
                    p0.clear()
                    addFirebaseLocations(p0)
                    p0.addMarker(
                        MarkerOptions().position(locationCoordinat).title(getString(R.string.yourHere))
                    )
                }
                markerOptions = MarkerOptions().position(LatLng(point.latitude, point.longitude))
                    .title(getString(R.string.newLocation))
                    .icon(
                        BitmapDescriptorFactory
                            .defaultMarker(
                                BitmapDescriptorFactory.HUE_ORANGE
                            )
                    )
                p0.addMarker(markerOptions)

                binding.latitudeTextview.text = point.latitude.toString()
                binding.longtitudeTextView.text = point.longitude.toString()
                binding.saveSelectedLocation.setOnClickListener {
                    if (!binding.id.text.toString().isEmpty()) {
                        selectedSafetyViewModel.saveSafetyLocation(
                            SafetyLocationModel(
                                binding.id.text.toString(),
                                binding.latitudeTextview.text.toString(),
                                binding.longtitudeTextView.text.toString()
                            )
                        )
                            .observe(
                                viewLifecycleOwner,
                                Observer
                                {
                                    if (it) {
                                        Toast.makeText(context, R.string.locationAddSuccess, Toast.LENGTH_SHORT).show()
                                        Navigation.findNavController(binding.root).navigate(R.id.action_mapsFragment_to_mapFragment)
                                    } else {
                                        Toast.makeText(context, R.string.locationAddFail, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                    } else {
                        Toast.makeText(context, R.string.giveLocationName, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        )
    }
    private fun addFirebaseLocations(p0: GoogleMap) {

        if (locationList.size == 0) {
            selectedSafetyViewModel.getSafetyLocations().observe(
                viewLifecycleOwner,
                Observer {
                    locationList.addAll(it)
                    drawIconForLocation(locationList, p0)
                }
            )
        } else {
            drawIconForLocation(locationList, p0)
        }
    }
    private fun drawIconForLocation(list: List<SafetyLocationModel>, p0: GoogleMap) {
        for (i in 0..list.size - 1) {
            val defaultMarker = MarkerOptions().position(
                LatLng(
                    list.get(i).latitude.toDouble(),
                    list.get(i).longtitude.toDouble()
                )
            )
                .title(list.get(i).name)
                .title(getString(R.string.newLocation))
                .icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                )
            p0.addMarker(defaultMarker)
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
    private fun onBackPressed() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("TAG", "handleOnBackPressed: ")
                    goBack()
                    // Leave empty do disable back press or
                    // write your code which you want
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )
    }
    private fun goBack() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_mapsFragment_to_mapFragment)
    }
}
