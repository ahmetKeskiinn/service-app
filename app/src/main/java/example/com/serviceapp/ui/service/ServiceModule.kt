package example.com.serviceapp.ui.service

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import example.com.mapproject.utils.ViewModelKey
import example.com.serviceapp.ui.service.feature.ChatServiceViewModel
import example.com.serviceapp.ui.service.feature.MainServiceViewModel

@Module
abstract class ServiceModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChatServiceViewModel::class)
    internal abstract fun bindServiceChatViewModel(viewModel: ChatServiceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainServiceViewModel::class)
    internal abstract fun bindServiceViewModel(viewModel: MainServiceViewModel): ViewModel
}
