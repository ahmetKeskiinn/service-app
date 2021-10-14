package example.com.serviceapp.ui.family.feature.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentFamilyBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.ui.MainActivity.Companion.ACTION_STOP_FOREGROUND
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.adapters.FamilyRecylerAdapter
import example.com.serviceapp.utils.animationHideDelay
import example.com.serviceapp.utils.animationStartDelay
import example.com.serviceapp.utils.services.ForegroundService
import example.com.serviceapp.utils.zoomCount
import javax.inject.Inject

class FamilyFragment : Fragment(), OnMapReadyCallback, PermissionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var androidService: ForegroundService
    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var familyViewModel: FamilyViewModel
    private lateinit var binding: FragmentFamilyBinding
    private lateinit var recyclerAdapter: FamilyRecylerAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFamilyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAnimationInComponents()
        initialUI()
        initialVM()
        initialTextViews()
        startService()
        getIconList()

        super.onViewCreated(view, savedInstanceState)
    }
    private fun getIconList() {
        val imageList = ArrayList<ImageView>()
        val colorList = ArrayList<Int>()
        imageList.add(binding.icBusImage)
        imageList.add(binding.icChatImage)
        imageList.add(binding.icLocationImage)
        imageList.add(binding.icAddChildrenImage)
        imageList.add(binding.icListStudentImage)
        colorList.add(R.color.busCardIconBorder)
        colorList.add(R.color.chatCardIconBorder)
        colorList.add(R.color.locationCardIconBorder)
        colorList.add(R.color.newStudentCardIconBorder)
        colorList.add(R.color.listStudentCardIconBorder)
        changeIconColor(imageList, colorList)
    }
    private fun changeIconColor(icons: ArrayList<ImageView>, colors: ArrayList<Int>) {
        for (i in 0..icons.size - 1) {
            context?.let { ContextCompat.getColor(it, colors.get(i)) }?.let {
                icons.get(i).setColorFilter(
                    it,
                    PorterDuff.Mode.MULTIPLY
                )
            }
        }
    }

    private fun setAnimationInComponents() {
        val animationSlideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_components)
        val animSlideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_components)
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.newStudentCardView.startAnimation(animSlideOut)
        binding.safetyLocationCardView.startAnimation(animationSlideIn)
        binding.chatCardView.startAnimation(animSlideOut)
        binding.whereBusCardView.startAnimation(animationSlideIn)
        binding.listOfStudentCardView.startAnimation(animationSlideIn)
        binding.topImagView.startAnimation(animationFadeIn)
        binding.topTw.startAnimation(animationFadeIn)
    }

    private fun listOfStudentButtonClicked() {
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        val animationSlideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_recycler)
        binding.newStudentCardView.startAnimation(animationFadeOut)
        binding.whereBusCardView.startAnimation(animationFadeOut)
        binding.safetyLocationCardView.startAnimation(animationFadeOut)
        Handler().postDelayed(
            {
                binding.newStudentCardView.isVisible = false
                binding.whereBusCardView.isVisible = false
                binding.safetyLocationCardView.isVisible = false
                binding.childrenCardView.startAnimation(animationSlideIn)
                binding.childrenCardView.isVisible = true
            },
            animationStartDelay
        )
    }

    private fun hideListOfButton() {
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        val animationSlideIn = AnimationUtils.loadAnimation(context, R.anim.slide_out_recycler)
        binding.childrenCardView.startAnimation(animationSlideIn)
        binding.childrenCardView.isVisible = false
        Handler().postDelayed(
            {
                binding.newStudentCardView.startAnimation(animationFadeOut)
                binding.whereBusCardView.startAnimation(animationFadeOut)
                binding.safetyLocationCardView.startAnimation(animationFadeOut)
                binding.newStudentCardView.isVisible = true
                binding.whereBusCardView.isVisible = true
                binding.safetyLocationCardView.isVisible = true
            },
            animationHideDelay
        )
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        familyViewModel = ViewModelProvider(this, viewModelFactory).get(FamilyViewModel::class.java)
    }

    private fun initialTextViews() {
        val view = layoutInflater.inflate(R.layout.where_is_the_bus_dialog, null)
        binding.whereBusCardView.setOnClickListener {
            val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
            val closeButton = view.findViewById<ImageView>(R.id.dismissButton)
            closeButton.setOnClickListener {
                if (dialog != null) {
                    (view.getParent() as ViewGroup).removeView(view) // <- fix
                    dialog.dismiss()
                }
            }
            if (dialog != null) {
                dialog.setCancelable(false)
            }
            if (dialog != null) {

                dialog.setContentView(view)
            }
            if (dialog != null) {
                dialog.show()
            }
            permissionCheck()
        }
        binding.newStudentCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_addChildrenFragment)
        }
        binding.safetyLocationCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_mapsFragment)
        }
        binding.chatCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_chatServiceFragment)
        }
        binding.listOfStudentCardView.setOnClickListener {
            initialListOfStudent()
        }
        binding.familyChildrenCloseButton.setOnClickListener {
            hideListOfButton()
            binding.listOfStudentCardView.isClickable = true
        }
    }

    private fun initialListOfStudent() {
        val view = layoutInflater.inflate(R.layout.list_of_student_dialog, null)
        val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
        val closeButton = view.findViewById<ImageView>(R.id.dissmissList)
        val recycler = view.findViewById<RecyclerView>(R.id.childrenRecyclerDialog)
        closeButton.setOnClickListener {
            if (dialog != null) {
                (view.getParent() as ViewGroup).removeView(view) // <- fix
                dialog.dismiss()
            }
        }
        if (dialog != null) {
            dialog.setCancelable(false)
        }
        if (dialog != null) {
            recyclerAdapter = FamilyRecylerAdapter()
            recycler.apply {
                layoutManager = LinearLayoutManager(this.context)
                setHasFixedSize(true)
                adapter = recyclerAdapter
            }
            getData()
            dialog.setContentView(view)
        }
        if (dialog != null) {
            dialog.show()
        }
    }
    private fun getData() {
        familyViewModel.getChildList().observe(
            viewLifecycleOwner,
            Observer
            {
                recyclerAdapter.submitList(it)
            }
        )
    }

    private fun startService() {
        //  val intentStop = Intent(activity, androidService::class.java)
        //  requireActivity().startService(Intent(intentStop))
    }

    private fun stopService() {
        val intentStop = Intent(activity, ForegroundService::class.java)
        intentStop.action = ACTION_STOP_FOREGROUND
        requireActivity().startService(intentStop)
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

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("TAG", "onMapReady: " + location?.latitude)
                Log.d("TAG", "onMapReady: " + location?.longitude)
                val coordinat = location?.latitude?.let { LatLng(it, location.longitude) }
                p0.addMarker(
                    MarkerOptions().position(coordinat).title(getString(R.string.yourHere))
                )
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
