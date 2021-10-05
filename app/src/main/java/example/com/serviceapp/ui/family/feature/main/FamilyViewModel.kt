package example.com.serviceapp.ui.family.feature.main

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.utils.children
import example.com.serviceapp.utils.idShared
import example.com.serviceapp.utils.nameSurname
import example.com.serviceapp.utils.parentName
import example.com.serviceapp.utils.schoolNumber
import example.com.serviceapp.utils.service
import javax.inject.Inject

class FamilyViewModel @Inject constructor(
    val fireBase: FirebaseAuth,
    val firebaseDatabase: FirebaseDatabase,
    val sharedPreferences: SharedPreferences
) : ViewModel() {
    companion object {
    }
    var data: MutableLiveData<List<AddChild>> = getChildList()
    fun getChildList(): MutableLiveData<List<AddChild>> {
        var returnData: MutableLiveData<List<AddChild>> = MutableLiveData<List<AddChild>>()
        val db = firebaseDatabase.getReference(children)
        val childrenList = ArrayList<AddChild>()
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                if (child.child(parentName).getValue().toString().equals(sharedPreferences.getString(idShared, ""))) {
                    childrenList.add(
                        AddChild(
                            child.child(nameSurname).getValue().toString(),
                            child.child(schoolNumber).getValue().toString(),
                            child.child(service).getValue().toString().toBoolean(),
                            child.child(parentName).getValue().toString()
                        )
                    )
                }
            }
            returnData.value = childrenList
        }.addOnFailureListener {
        }

        return returnData
    }
}
