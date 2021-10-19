package example.com.serviceapp.ui.family.feature.safetyLocation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.utils.latitude
import example.com.serviceapp.utils.longtitude
import example.com.serviceapp.utils.name
import example.com.serviceapp.utils.safetyLocation
import javax.inject.Inject

class SelectedSafetyLocationViewModel @Inject constructor(
    val fireBase: FirebaseAuth,
    val firebaseDatabase: FirebaseDatabase,
) : ViewModel() {
    fun saveSafetyLocation(model: SafetyLocationModel): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        val db = firebaseDatabase.getReference(safetyLocation)
        db.child(model.name.toString()).setValue(model).addOnCompleteListener { task ->
            isSuccess.setValue(task.isSuccessful)
        }
        return isSuccess
    }
    fun getSafetyLocations(): MutableLiveData<List<SafetyLocationModel>> {
        val locationList = MutableLiveData<List<SafetyLocationModel>>()
        val list = ArrayList<SafetyLocationModel>()
        val db = firebaseDatabase.getReference(safetyLocation)
        db.get().addOnSuccessListener {
            for (child in it.children) {
                var listElementName = child.child(name).getValue().toString()
                var listElementLatitude = child.child(latitude).getValue().toString()
                var listElementLongtitude = child.child(longtitude).getValue().toString()
                list.add(SafetyLocationModel(listElementName, listElementLatitude, listElementLongtitude))
            }
            Log.d("TAG", "getSafetyLocations: " + list)
            locationList.value = list
        }
        return locationList
    }
}
