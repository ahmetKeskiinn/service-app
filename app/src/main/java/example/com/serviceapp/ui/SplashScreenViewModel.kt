package example.com.serviceapp.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import example.com.serviceapp.utils.Authentication
import example.com.serviceapp.utils.AuthenticationSplash
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(val auth:FirebaseAuth, val sharedPreferences: SharedPreferences):ViewModel() {


    fun getInformations(listener: AuthenticationSplash){
        if(!sharedPreferences.getString("id","").toString().isEmpty() && !sharedPreferences.getString("pw","").toString().isEmpty()){
            val infos = sharedPreferences.getString("id","").toString() + ","+ sharedPreferences.getString("pw","").toString()
            listener.isSuccess(infos,true)
        } else{
            listener.isSuccess("",false)
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
}