package example.com.serviceapp.ui.family.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentFamilyBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import javax.inject.Inject

class FamilyFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var familyViewModel: FamilyViewModel
    private lateinit var binding: FragmentFamilyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFamilyBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        initialTextViews()
        initialRecyclerView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }
    private fun initialVM() {
        familyViewModel = ViewModelProvider(this, viewModelFactory).get(FamilyViewModel::class.java)
    }
    private fun initialTextViews() {
        binding.addChildrenButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_addChildrenFragment)
        }
        binding.addLocationButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mapFragment_to_selectSafetyLocationFragment)
        }
    }
    private fun initialRecyclerView() {
        binding.childrenRecycler
    }
}
