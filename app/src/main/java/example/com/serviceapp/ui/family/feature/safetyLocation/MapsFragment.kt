package example.com.serviceapp.ui.family.feature.safetyLocation

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentMapsBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.zoomCount
import javax.inject.Inject

class MapsFragment : Fragment(), OnMapReadyCallback {
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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initialUI()
        initialVM()
    }
    private fun initialUI() {
        MyApp.appComponent.inject(this)
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
                val coordinat = location?.latitude?.let { LatLng(it, location.longitude) }
                p0.addMarker(MarkerOptions().position(coordinat).title(getString(R.string.yourHere)))
                p0.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinat, zoomCount))
            }
    }
}
