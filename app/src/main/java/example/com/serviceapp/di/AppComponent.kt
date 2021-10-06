package example.com.mapproject.di

import dagger.Component
import example.com.serviceapp.di.AppModule
import example.com.serviceapp.di.FactoryModule
import example.com.serviceapp.di.FireBaseModule
import example.com.serviceapp.di.NetworkModule
import example.com.serviceapp.di.RoomModule
import example.com.serviceapp.di.SharedPrefModule
import example.com.serviceapp.ui.LoginViewModelModule
import example.com.serviceapp.ui.SplashFragment
import example.com.serviceapp.ui.SplashScreenModule
import example.com.serviceapp.ui.chat.ChatFragment
import example.com.serviceapp.ui.chat.ChatModule
import example.com.serviceapp.ui.family.FamilyModule
import example.com.serviceapp.ui.family.feature.addChild.AddChildrenFragment
import example.com.serviceapp.ui.family.feature.main.FamilyFragment
import example.com.serviceapp.ui.family.feature.safetyLocation.SelectSafetyLocationFragment
import example.com.serviceapp.ui.login.LoginFragment
import example.com.serviceapp.ui.service.ServiceModule
import example.com.serviceapp.ui.service.feature.MainServiceFragment
import example.com.serviceapp.ui.teacher.TeacherModule
import example.com.serviceapp.ui.teacher.feature.MainAdminFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        AppModule::class,
        FactoryModule::class,
        RoomModule::class,
        LoginViewModelModule::class,
        FamilyModule::class,
        ServiceModule::class,
        TeacherModule::class,
        ChatModule::class,
        FireBaseModule::class,
        SharedPrefModule::class,
        SplashScreenModule::class
    ]
)
interface AppComponent {
    fun inject(splashScreen: SplashFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(addChildrenFragment: AddChildrenFragment)
    fun inject(familyFragment: FamilyFragment)
    fun inject(selectSafetyLocationFragment: SelectSafetyLocationFragment)
    fun inject(chatServiceFragment: ChatFragment)
    fun inject(mainServiceFragment: MainServiceFragment)
    fun inject(mainAdminFragment: MainAdminFragment)
}
