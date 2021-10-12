package example.com.serviceapp.ui.family.feature.main

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentFamilyBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.ui.MainActivity.Companion.ACTION_STOP_FOREGROUND
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.adapters.FamilyRecylerAdapter
import example.com.serviceapp.utils.services.ForegroundService
import javax.inject.Inject

class FamilyFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var androidService: ForegroundService
    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var familyViewModel: FamilyViewModel
    private lateinit var binding: FragmentFamilyBinding
    private lateinit var recyclerAdapter: FamilyRecylerAdapter

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
        initialRecycler()
        getData()
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
            1000
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
            500
        )
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        familyViewModel = ViewModelProvider(this, viewModelFactory).get(FamilyViewModel::class.java)
    }

    private fun initialTextViews() {
        binding.newStudentCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_addChildrenFragment)
        }
        binding.safetyLocationCardView.setOnClickListener {
            //Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_selectSafetyLocationFragment)
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_mapsFragment)
        }
        binding.chatCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_chatServiceFragment)
        }
        binding.listOfStudentCardView.setOnClickListener {
            listOfStudentButtonClicked()
            binding.listOfStudentCardView.isClickable = false
        }
        binding.familyChildrenCloseButton.setOnClickListener {
            hideListOfButton()
            binding.listOfStudentCardView.isClickable = true
        }
    }

    private fun initialRecycler() {
        recyclerAdapter = FamilyRecylerAdapter()
        binding.childrenRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = recyclerAdapter
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
        val intentStop = Intent(activity, androidService::class.java)
        requireActivity().startService(Intent(intentStop))
    }

    private fun stopService() {
        val intentStop = Intent(activity, ForegroundService::class.java)
        intentStop.action = ACTION_STOP_FOREGROUND
        requireActivity().startService(intentStop)
    }
}
