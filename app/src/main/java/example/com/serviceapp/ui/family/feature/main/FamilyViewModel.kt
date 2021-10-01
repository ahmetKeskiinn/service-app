package example.com.serviceapp.ui.family.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class FamilyViewModel @Inject constructor(val fireBase: FirebaseAuth, val firebaseDatabase: FirebaseDatabase) : ViewModel(){

    /*fun getResponseFromRealtimeDatabaseUsingCallback(callback: FirebaseCallback) {
        productRef.get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.products = result.children.map { snapShot ->
                        snapShot.getValue(Product::class.java)!!
                    }
                }
            } else {
                response.exception = task.exception
            }
            callback.onResponse(response)
        }
    }*/

    fun childList(){
        val db = firebaseDatabase.getReference("children")
        db.get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
        }
    }
}
