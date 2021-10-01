package example.com.mapproject.di

import dagger.Component
import example.com.serviceapp.di.FactoryModule
import example.com.serviceapp.di.FireBaseModule
import example.com.serviceapp.di.NetworkModule
import example.com.serviceapp.di.RoomModule
import example.com.serviceapp.di.SharedPrefModule
import example.com.serviceapp.ui.LoginViewModelModule
import example.com.serviceapp.ui.family.FamilyModule
import example.com.serviceapp.ui.family.feature.addChild.AddChildrenFragment
import example.com.serviceapp.ui.family.feature.main.FamilyFragment
import example.com.serviceapp.ui.family.feature.safetyLocation.SelectSafetyLocationFragment
import example.com.serviceapp.ui.login.LoginFragment
import example.com.serviceapp.ui.service.ServiceModule
import example.com.serviceapp.ui.service.feature.ChatServiceFragment
import example.com.serviceapp.ui.service.feature.MainServiceFragment
import example.com.serviceapp.ui.teacher.TeacherModule
import example.com.serviceapp.ui.teacher.feature.MainAdminFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        FactoryModule::class,
        RoomModule::class,
        SharedPrefModule::class,
        LoginViewModelModule::class,
        FamilyModule::class,
        ServiceModule::class,
        TeacherModule::class,
        FireBaseModule::class
    ]
)
interface AppComponent {
    fun inject(loginFragment: LoginFragment)
    fun inject(addChildrenFragment: AddChildrenFragment)
    fun inject(familyFragment: FamilyFragment)
    fun inject(selectSafetyLocationFragment: SelectSafetyLocationFragment)
    fun inject(chatServiceFragment: ChatServiceFragment)
    fun inject(mainServiceFragment: MainServiceFragment)
    fun inject(mainAdminFragment: MainAdminFragment)
}
