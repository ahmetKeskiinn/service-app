package example.com.serviceapp.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.utils.authenticationUtils.login.Authentication
import example.com.serviceapp.utils.authenticationUtils.login.AuthenticationSplash
import example.com.serviceapp.utils.authenticationUtils.login.AuthenticationStatus
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(val auth: FirebaseAuth, val firebaseDB: FirebaseDatabase, val sharedPreferences: SharedPreferences) : ViewModel() {

    fun getInformations(listener: AuthenticationSplash) {
        if (!sharedPreferences.getString("id", "").toString().isEmpty() && !sharedPreferences.getString("pw", "").toString().isEmpty()) {
            val infos = sharedPreferences.getString("id", "").toString() + "," + sharedPreferences.getString("pw", "").toString()
            listener.isSuccess(infos, true)
        } else {
            listener.isSuccess("", false)
        }
    }

    fun firebaseAuth(listener: Authentication, eMail: String, password: String) {
        if (eMail.isEmpty() || password.isEmpty()) {
            listener.isSuccess(false)
        } else {
            auth.signInWithEmailAndPassword(eMail, password)
                .addOnCompleteListener { task ->
                    listener.isSuccess(task.isSuccessful)
                }
        }
    }
    fun getStatus(listener: AuthenticationStatus) {
        Log.d("TAG", "getStatus: " + sharedPreferences.getString("id", ""))
        val db = firebaseDB.getReference("users")
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                if (child.child("userName").getValue().toString().equals(sharedPreferences.getString("id", ""))) {
                    Log.d("TAG", "getStatus: ")
                    listener.getStatus(child.child("status").getValue().toString())
                }
            }
        }.addOnFailureListener {
        }
    }
}
