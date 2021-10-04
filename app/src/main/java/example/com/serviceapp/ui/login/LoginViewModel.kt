package example.com.serviceapp.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.utils.authenticationUtils.Authentication
import example.com.serviceapp.utils.authenticationUtils.AuthenticationStatus
import javax.inject.Inject

class LoginViewModel @Inject constructor(val auth: FirebaseAuth, val firebaseDB: FirebaseDatabase, val sharedPreferences: SharedPreferences) : ViewModel() {
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
    fun saveInfos(id: String, pw: String) {
        sharedPreferences.edit().putString("id", id).apply()
        sharedPreferences.edit().putString("pw", pw).apply()
    }
    fun getStatus(listener: AuthenticationStatus) {
        Log.d("TAG", "getStatus: " + sharedPreferences.getString("id", ""))
        val db = firebaseDB.getReference("users")
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                if (child.child("userName").getValue().toString().equals(auth.currentUser?.email)) {
                    Log.d("TAG", "getStatus: ")
                    listener.getStatus(child.child("status").getValue().toString())
                }
            }
        }.addOnFailureListener {
        }
    }
}
