package example.com.serviceapp.di

import android.app.Application
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import example.com.mapproject.di.AppComponent
import example.com.mapproject.di.DaggerAppComponent
import javax.inject.Inject

class MyApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().networkModule(NetworkModule(this)).roomModule(RoomModule(this)).sharedPrefModule(SharedPrefModule()).build()
    }
}
