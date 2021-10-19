package example.com.serviceapp.ui.family.feature.main

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
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.maps.model.MapStyleOptions
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
import example.com.serviceapp.utils.adapters.GridLayoutAdapter
import example.com.serviceapp.utils.adapters.LayoutAdapter
import example.com.serviceapp.utils.adapters.LayoutClickListener
import example.com.serviceapp.utils.services.ForegroundService
import example.com.serviceapp.utils.zoomCount
import javax.inject.Inject


class FamilyFragment : Fragment(), OnMapReadyCallback, PermissionListener, LayoutClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var androidService: ForegroundService
    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var familyViewModel: FamilyViewModel
    private lateinit var binding: FragmentFamilyBinding
    private lateinit var recyclerAdapter: FamilyRecylerAdapter
    private lateinit var layoutAdapter: LayoutAdapter
    private lateinit var gridLayoutManager: GridLayoutAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var whereBusView: View

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
        initialRecycler()
        initialGridRecycler()
        super.onViewCreated(view, savedInstanceState)
    }
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    private fun initialRecycler() {
        val listLayoutItems = ArrayList<LayoutModel>()
        listLayoutItems.add(
                LayoutModel(
                        getString(R.string.where),
                        getString(R.string.whereSub),
                        R.drawable.ic_arrow_icon_family_small,
                        R.drawable.bus_background_shape,
                        R.drawable.ic_family_ui_icon_background,
                        R.drawable.ic_family_ui_icon_background_yellow_big_small,
                        R.drawable.ic_where_is_the_bus_icon_in_family
                )
        )

        listLayoutItems.add(
                LayoutModel(
                        getString(R.string.chat),
                        getString(R.string.chatSub),
                        R.drawable.ic_arrow_icon_family_small,
                        R.drawable.chat_background_shape,
                        R.drawable.ic_family_ui_icon_background,
                        R.drawable.ic_family_ui_icon_background_purple_big_small,
                        R.drawable.ic_chat_icon_in_family
                )
        )
        listLayoutItems.add(
                LayoutModel(
                        getString(
                                R.string.addNewStudent
                        ),
                        getString(R.string.addSafetyLcation),
                        R.drawable.ic_arrow_icon_family_small,
                        R.drawable.add_location_background_shape,
                        R.drawable.ic_family_ui_icon_background,
                        R.drawable.ic_family_ui_icon_background_blue_big_small,
                        R.drawable.ic_location_icon_in_family
                )
        )
        layoutAdapter = LayoutAdapter(this)
        binding.layoutRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = layoutAdapter
        }
        layoutAdapter.submitList(listLayoutItems)
    }
    private fun initialGridRecycler() {
        val gridLayoutItems = ArrayList<LayoutModel>()
        gridLayoutItems.add(
                LayoutModel(
                        getString(R.string.addNewStudentSub),
                        getString(R.string.addNewStudent),
                        R.drawable.ic_arrow_icon_family_small,
                        R.drawable.add_new_student_background_shape,
                        R.drawable.ic_family_ui_icon_background_small_big,
                        R.drawable.id_family_ui_icon_background_red_small_small,
                        R.drawable.ic_addd_children_icon_in_family
                )
        )

        gridLayoutItems.add(
                LayoutModel(
                        getString(R.string.list),
                        getString(R.string.student),
                        R.drawable.ic_arrow_icon_family_small,
                        R.drawable.list_of_student_background_shape,
                        R.drawable.ic_family_ui_icon_background_small_big,
                        R.drawable.id_family_ui_icon_background_blue_small_small,
                        R.drawable.ic_list_of_student_icon_in_family
                )
        )

        gridLayoutManager = GridLayoutAdapter(this)
        binding.layoutGridRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = gridLayoutManager
        }
        gridLayoutManager.submitList(gridLayoutItems)
    }

    private fun setAnimationInComponents() {
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        val animationSlideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_components)
        val animationSlideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_components)
        binding.layoutGridRecycler.startAnimation(animationSlideOut)
        binding.layoutRecycler.startAnimation(animationSlideIn)
        binding.topImagView.startAnimation(animationFadeIn)
        binding.topTw.startAnimation(animationFadeIn)
    }
    private fun initialUI() {
        whereBusView = layoutInflater.inflate(R.layout.dialog_where_is_the_bus, null)

        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        familyViewModel = ViewModelProvider(this, viewModelFactory).get(FamilyViewModel::class.java)
    }

    private fun checkClickedLayout(cardView: LayoutModel) {
        if (
            cardView.title.equals(getString(R.string.where))
        ) {

            val dialog = context?.let { it1 ->
                BottomSheetDialog(
                        it1,
                        R.style.BottomSheetDialogTheme
                )
            }
            val closeButton = whereBusView.findViewById<ImageView>(R.id.dismissButton)
            closeButton.setOnClickListener {
                if (dialog != null) {
                    dialog.cancel()
                    (whereBusView.getParent() as ViewGroup).removeView(whereBusView)
                }
            }
            if (dialog != null) {
                dialog.setCancelable(false)
            }
            if (dialog != null) {
                dialog.setContentView(whereBusView)
            }
            if (dialog != null) {
                dialog.show()
            }
            permissionCheck()
        } else if (cardView.title.equals(getString(R.string.chat))) {
            Navigation
                .findNavController(binding.root)
                .navigate(R.id.action_mapFragment_to_chatServiceFragment)
        } else if (cardView.subTitle.equals(getString(R.string.addSafetyLcation))) {
            Navigation
                .findNavController(binding.root)
                .navigate(R.id.action_mapFragment_to_mapsFragment)
        } else if (cardView.title.equals(getString(R.string.addNewStudentSub))) {
            Navigation
                .findNavController(binding.root)
                .navigate(R.id.action_mapFragment_to_addChildrenFragment)
        } else if (cardView.title.equals(getString(R.string.list))) {
            initialListOfStudent()
        }
    }

    private fun initialListOfStudent() {
        val view = layoutInflater.inflate(R.layout.dialog_list_of_student, null)
        view.background = context?.getDrawable(R.color.recyclerBackground)
        val dialog = context?.let { it1 ->
            BottomSheetDialog(
                    it1,
                    R.style.BottomSheetDialogTheme
            )
        }
        val closeButton = view.findViewById<ImageView>(R.id.dissmissList)
        val recycler = view.findViewById<RecyclerView>(R.id.childrenRecyclerDialog)
        closeButton.setOnClickListener {
            if (dialog != null) {
                (view.getParent() as ViewGroup).removeView(view)
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
        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(activity, R.raw.googlemapstyle)
        p0.setMapStyle(mapStyleOptions)
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

    override fun clickedLayout(cardView: LayoutModel) {
        checkClickedLayout(cardView)
    }
}
