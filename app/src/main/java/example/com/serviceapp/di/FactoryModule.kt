package example.com.serviceapp.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import example.com.serviceapp.utils.ViewModelFactory

@Module
abstract class FactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
