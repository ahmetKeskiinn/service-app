package example.com.serviceapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class SharedPrefModule {
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences? {
        return application.getSharedPreferences("login", Context.MODE_PRIVATE)
    }
}
