package example.com.serviceapp.ui.family.feature.addChild

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentAddChildrenBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import javax.inject.Inject

class AddChildrenFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var addChildrenFragment: AddChildrenViewModel
    private lateinit var binding: FragmentAddChildrenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddChildrenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        addInfos()
        initalButton()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        addChildrenFragment = ViewModelProvider(this, viewModelFactory).get(AddChildrenViewModel::class.java)
    }

    private fun addInfos() {
        // if(binding)
    }

    private fun initalButton() {
        binding.addButton.setOnClickListener {
            addChildrenFragment.addChild(AddChild(binding.childrenName.text.toString(),binding.childrenNumber.text.toString(),binding.serviceCheckBox.isChecked,null))
            //Navigation.findNavController(it).navigate(R.id.action_addChildrenFragment_to_mapFragment)
        }
    }
}