package example.com.serviceapp.ui.family.feature.addChild

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import example.com.serviceapp.R
import example.com.serviceapp.databinding.FragmentAddChildrenBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import example.com.serviceapp.utils.default
import example.com.serviceapp.utils.intentType
import javax.inject.Inject

class AddChildrenFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var addChildrenFragmentViewModel: AddChildrenViewModel
    private lateinit var binding: FragmentAddChildrenBinding
    private val IMAGE_REQUEST: Int = 1
    private lateinit var progressDialog: ProgressDialog
    private var uri: String = ""
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
        progressDialog = ProgressDialog(context)
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        addChildrenFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(AddChildrenViewModel::class.java)
    }

    private fun hideAnimationInComponents() {
        val animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        binding.childrenNumber.startAnimation(animationFadeIn)
        binding.addButton.startAnimation(animationFadeIn)
        binding.childPhoto.startAnimation(animationFadeIn)
        binding.childrenName.startAnimation(animationFadeIn)
        binding.serviceCheckBox.startAnimation(animationFadeIn)
    }

    private fun openImage() {
        val intent = Intent()
        intent.type = intentType
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            intent,
            IMAGE_REQUEST
        )
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data?.data != null) {
            uri = data.data.toString()
            binding.childPhoto.setImageURI(data.data)
        }
        progressDialog.dismiss()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initalButton() {
        binding.addButton.setOnClickListener {
            val childrenName = binding.childrenNameEdittext.text.toString()
            val childrenNumber = binding.childrenNumberEditText.text.toString()
            if (!childrenName.isEmpty() && !childrenName.isBlank() && !childrenName.isEmpty() && !childrenNumber.isBlank()) {
                hideAnimationInComponents()
                if (uri.equals("")) {
                    uri = default
                }
                addChildrenFragmentViewModel
                    .setChildrenImage(
                        uri.toUri(),
                        childrenName,
                        childrenNumber
                    )
                    .observe(
                        viewLifecycleOwner,
                        Observer {
                            if (!it.equals(default)) {
                                Toast.makeText(
                                    context,
                                    R.string.uploadImageSuccess,
                                    Toast.LENGTH_SHORT
                                ).show()
                                addChildren(it)
                            } else {
                                Toast.makeText(
                                    context,
                                    R.string.uploadImageFail,
                                    Toast.LENGTH_SHORT
                                ).show()
                                addChildren(it)
                            }
                        }
                    )
            } else {
                Toast.makeText(context, R.string.wrongStudentInfo, Toast.LENGTH_SHORT).show()
            }
        }
        binding.clickForReplaceImage.setOnClickListener {
            openImage()
        }
        binding.childPhoto.setOnClickListener {
            openImage()
        }
    }
    private fun addChildren(url: String) {
        addChildrenFragmentViewModel.addChild(
            AddChild(
                binding.childrenNameEdittext.text.toString(),
                binding.childrenNumberEditText.text.toString(),
                binding.serviceCheckBox.isChecked,
                null,
                url
            )
        ).observe(
            viewLifecycleOwner,
            Observer
            {
                if (it) {
                    Toast.makeText(context, R.string.requestToast, Toast.LENGTH_SHORT)
                        .show()
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_addChildrenFragment_to_mapFragment)
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.somethingWentsWrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
}
