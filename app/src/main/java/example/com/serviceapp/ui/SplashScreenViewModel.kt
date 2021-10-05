package example.com.serviceapp.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.utils.authenticationUtils.login.Authentication
import example.com.serviceapp.utils.authenticationUtils.login.AuthenticationSplash
import example.com.serviceapp.utils.authenticationUtils.login.AuthenticationStatus
import example.com.serviceapp.utils.idShared
import example.com.serviceapp.utils.status
import example.com.serviceapp.utils.userName
import example.com.serviceapp.utils.users
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
        val db = firebaseDB.getReference(users)
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                if (child.child(userName).getValue().toString().equals(sharedPreferences.getString(idShared, ""))) {
                    listener.getStatus(child.child(status).getValue().toString())
                }
            }
        }.addOnFailureListener {
        }
    }
}
