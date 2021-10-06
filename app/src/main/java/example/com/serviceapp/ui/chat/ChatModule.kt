package example.com.serviceapp.ui.chat

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import example.com.mapproject.utils.ViewModelKey

@Module
abstract class ChatModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    internal abstract fun bindServiceChatViewModel(viewModel: ChatViewModel): ViewModel
}
