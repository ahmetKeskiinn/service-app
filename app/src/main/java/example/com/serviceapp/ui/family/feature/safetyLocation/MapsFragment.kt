package example.com.serviceapp.ui.family.feature.safetyLocation

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import example.com.serviceapp.utils.serviceLocation
import example.com.serviceapp.utils.services.ServiceLocationModel
import javax.inject.Inject

class MapsFragment : Fragment(),OnMapReadyCallback {
    private lateinit var binding:FragmentMapsBinding
    private lateinit var selectedSafetyViewModel: SelectedSafetyLocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @Inject
    lateinit var viewmodelfactory:ViewModelFactory
   /* private val callback = OnMapReadyCallback { googleMap ->
        val coordinat = LatLng(0.0, 0.0)
        googleMap.addMarker(MarkerOptions().position(coordinat).title(""))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinat))
        val zoomLevel = 16.0f //This goes up to 21
      //  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinat, zoomLevel))
    }*/

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
    private fun obtieneLocalizacion(): LiveData<ArrayList<String>> {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        val coordinatList = MutableLiveData<ArrayList<String>>()
        val list = ArrayList<String>()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("TAG", "obtieneLocalizacion: " + location?.latitude)
                Log.d("TAG", "obtieneLocalizacion: " + location?.altitude)
                list.add(location?.altitude.toString())
              //  coordinatList.add(location?.latitude.toString())
            }
        return coordinatList
    }

    override fun onMapReady(p0: GoogleMap) {
        val coordinat = LatLng(55.0, 60.0)
        p0.addMarker(MarkerOptions().position(coordinat).title(""))
        p0.moveCamera(CameraUpdateFactory.newLatLng(coordinat))
        val zoomLevel = 16.0f //This goes up to 21
        //  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinat, zoomLevel))
    }

}
