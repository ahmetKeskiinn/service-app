package example.com.serviceapp.ui.family.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class FamilyViewModel @Inject constructor(val fireBase: FirebaseAuth, val firebaseDatabase: FirebaseDatabase) : ViewModel() {

    fun childList() {
        val db = firebaseDatabase.getReference("children")
        db.get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener {
        }
    }
}
