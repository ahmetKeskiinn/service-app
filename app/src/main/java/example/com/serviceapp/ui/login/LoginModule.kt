package example.com.serviceapp.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import example.com.mapproject.utils.ViewModelKey
import example.com.serviceapp.ui.login.LoginSource
import example.com.serviceapp.ui.login.LoginViewModel
import example.com.serviceapp.utils.GetService
import javax.inject.Singleton

@Module
class LoginModule {
    @Provides
    @Singleton
    internal fun providePopularMovies(
        api: GetService
    ): LoginSource = LoginSource(api)
}

@Module
abstract class LoginViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}
