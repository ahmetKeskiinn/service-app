package example.com.serviceapp.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import example.com.mapproject.utils.ViewModelKey

@Module
abstract class SplashScreenModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashScreenViewModel::class)
    internal abstract fun bindSplashScreenViewModel(viewModel: SplashScreenViewModel): ViewModel
}
