package example.com.serviceapp.ui.family.feature.main

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentFamilyBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.adapters.FamilyRecylerAdapter
import javax.inject.Inject

class FamilyFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
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
        super.onViewCreated(view, savedInstanceState)
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
    private fun listOfStudentButtonClicked(){
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        val animationSlideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_recycler)
        binding.newStudentCardView.startAnimation(animationFadeOut)
        binding.whereBusCardView.startAnimation(animationFadeOut)
        binding.safetyLocationCardView.startAnimation(animationFadeOut)
        Handler().postDelayed({
            binding.newStudentCardView.isVisible = false
            binding.whereBusCardView.isVisible = false
            binding.safetyLocationCardView.isVisible = false
            binding.childrenCardView.startAnimation(animationSlideIn)
            binding.childrenCardView.isVisible = true
        }, 1000)

    }

    private fun hideListOfButton(){
        val animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        val animationSlideIn = AnimationUtils.loadAnimation(context, R.anim.slide_out_recycler)
        binding.newStudentCardView.startAnimation(animationFadeOut)
        binding.whereBusCardView.startAnimation(animationFadeOut)
        binding.safetyLocationCardView.startAnimation(animationFadeOut)
        Handler().postDelayed({
            binding.newStudentCardView.isVisible = true
            binding.whereBusCardView.isVisible = true
            binding.safetyLocationCardView.isVisible = true
            binding.childrenCardView.startAnimation(animationSlideIn)
            binding.childrenCardView.isVisible = false
        }, 1000)

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
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_selectSafetyLocationFragment)
        }
        binding.chatCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_chatServiceFragment)
        }
        binding.listOfStudentCardView.setOnClickListener{
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
}
