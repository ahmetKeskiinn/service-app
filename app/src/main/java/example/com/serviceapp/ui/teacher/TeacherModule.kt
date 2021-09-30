package example.com.serviceapp.ui.teacher

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import example.com.mapproject.utils.ViewModelKey
import example.com.serviceapp.ui.teacher.feature.MainAdminViewModel

@Module
abstract class TeacherModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainAdminViewModel::class)
    internal abstract fun bindAdminViewModel(viewModel: MainAdminViewModel): ViewModel
}
