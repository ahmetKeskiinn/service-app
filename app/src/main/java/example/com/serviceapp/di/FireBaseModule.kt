package example.com.serviceapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FireBaseModule {
    @Provides
    @Singleton
    fun providesFireBaseAuth(): FirebaseAuth {
        val firebase = Firebase.auth
        return firebase
    }
}
