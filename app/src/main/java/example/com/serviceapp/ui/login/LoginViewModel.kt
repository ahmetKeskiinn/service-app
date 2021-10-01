package example.com.serviceapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import example.com.serviceapp.utils.Authentication
import javax.inject.Inject

class LoginViewModel @Inject constructor(val auth: FirebaseAuth) : ViewModel() {
    fun firebaseAuth(listener: Authentication, eMail: String, password: String) {
        if (eMail.isEmpty() || password.isEmpty()) {
            listener.isSuccess(false)
        } else {
            auth.signInWithEmailAndPassword(eMail, password)
                .addOnCompleteListener { task ->
                    listener.isSuccess(task.isSuccessful)
                }
        }
        Log.d("TAG", "childList: " + auth.currentUser?.email)
        Log.d("TAG", "childList: " + auth.currentUser?.displayName)
    }
}
