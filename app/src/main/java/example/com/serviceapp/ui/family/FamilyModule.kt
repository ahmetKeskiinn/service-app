package example.com.serviceapp.ui.family

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import example.com.mapproject.utils.ViewModelKey
import example.com.serviceapp.ui.family.feature.AddChildrenViewModel
import example.com.serviceapp.ui.family.feature.FamilyViewModel
import example.com.serviceapp.ui.family.feature.SelectedSafetyLocationViewModel

@Module
abstract class FamilyModule {
    @Binds
    @IntoMap
    @ViewModelKey(AddChildrenViewModel::class)
    internal abstract fun bindAddChildrenViewModel(viewModel: AddChildrenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FamilyViewModel::class)
    internal abstract fun bindFamilyViewModel(viewModel: FamilyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectedSafetyLocationViewModel::class)
    internal abstract fun bindSelectedSafetyViewModel(viewModel: SelectedSafetyLocationViewModel): ViewModel
}
