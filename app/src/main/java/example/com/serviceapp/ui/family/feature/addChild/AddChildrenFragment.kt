package example.com.serviceapp.ui.family.feature.addChild

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentAddChildrenBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.authenticationUtils.admin.AddChildren
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
        initalButton()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        addChildrenFragment = ViewModelProvider(this, viewModelFactory).get(AddChildrenViewModel::class.java)
    }

    private fun hideAnimationInComponents() {
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        binding.childrenNumber.startAnimation(animationFadeIn)
        binding.addButton.startAnimation(animationFadeIn)
        binding.childPhoto.startAnimation(animationFadeIn)
        binding.childrenName.startAnimation(animationFadeIn)
        binding.serviceCheckBox.startAnimation(animationFadeIn)
    }

    private fun initalButton() {
        binding.addButton.setOnClickListener {
            hideAnimationInComponents()
            addChildrenFragment.addChild(
                AddChild(
                    binding.childrenName.text.toString(),
                    binding.childrenNumber.text.toString(),
                    binding.serviceCheckBox.isChecked,
                    null
                )
            ).observe(viewLifecycleOwner, Observer {
                if (it) {
                    Toast.makeText(context, R.string.requestToast, Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root).navigate(R.id.action_addChildrenFragment_to_mapFragment)
                } else {
                    Toast.makeText(context, getString(R.string.somethingWentsWrong), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
