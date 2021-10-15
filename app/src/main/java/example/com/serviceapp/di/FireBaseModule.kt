package example.com.serviceapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FireBaseModule {
    @Provides
    @Singleton
    fun providesFireBase(): Firebase {
        return Firebase
    }
    @Provides
    @Singleton
    fun providesFireBaseDataBase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
    @Provides
    @Singleton
    fun providesFireBaseAuth(firebase: Firebase): FirebaseAuth {
        return firebase.auth
    }
    @Provides
    @Singleton
    fun providesStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().getReference("childrenPhotos")
    }
    @Provides
    @Singleton
    fun providesFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
