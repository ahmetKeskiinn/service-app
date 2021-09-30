package example.com.serviceapp.ui.service.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import example.com.serviceapp.databinding.FragmentChatServiceBinding
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.ViewModelFactory
import javax.inject.Inject

class ChatServiceFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var chatServiceViewModel: ChatServiceViewModel
    private lateinit var binding: FragmentChatServiceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }
    private fun initialVM() {
        chatServiceViewModel = ViewModelProvider(this, viewModelFactory).get(ChatServiceViewModel::class.java)
    }
}
