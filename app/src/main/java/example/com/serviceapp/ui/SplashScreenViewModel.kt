package example.com.serviceapp.ui

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.utils.idShared
import example.com.serviceapp.utils.status
import example.com.serviceapp.utils.userName
import example.com.serviceapp.utils.users
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(val auth: FirebaseAuth, val firebaseDB: FirebaseDatabase, val sharedPreferences: SharedPreferences) : ViewModel() {

    fun getInformations(): MutableLiveData<String> {
        val getInfos = MutableLiveData<String>()
        if (!sharedPreferences.getString("id", "").toString().isEmpty() && !sharedPreferences.getString("pw", "").toString().isEmpty()) {
            val infos = sharedPreferences.getString("id", "").toString() + "," + sharedPreferences.getString("pw", "").toString()
            getInfos.value = infos
        } else {
            getInfos.value = ""
        }
        return getInfos
    }

    fun firebaseAuth(eMail: String, password: String): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        if (eMail.isEmpty() || password.isEmpty()) {
            isSuccess.value = (false)
        } else {
            auth.signInWithEmailAndPassword(eMail, password)
                .addOnCompleteListener { task ->
                    isSuccess.value = (task.isSuccessful)
                }
        }
        return isSuccess
    }
    fun getStatus(): MutableLiveData<String> {
        val personStatus = MutableLiveData<String>()
        val db = firebaseDB.getReference(users)
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                if (child.child(userName).getValue().toString().equals(sharedPreferences.getString(idShared, ""))) {
                    personStatus.value = child.child(status).getValue().toString()
                }
            }
        }.addOnFailureListener {
            personStatus.value = it.toString()
        }
        return personStatus
    }
}
