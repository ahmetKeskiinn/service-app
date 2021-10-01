package example.com.serviceapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FireBaseModule {
    @Provides
    @Singleton
    fun providesFireBase(): Firebase{
        return Firebase
    }
    @Provides
    @Singleton
    fun providesFireBaseDataBase():FirebaseDatabase{
        return FirebaseDatabase.getInstance()
    }
    @Provides
    @Singleton
    fun providesFireBaseAuth(firebase:Firebase): FirebaseAuth {
        return firebase.auth
    }
}
