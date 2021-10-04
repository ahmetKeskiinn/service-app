package example.com.serviceapp.ui.family.feature.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.utils.family.ChildrenData
import javax.inject.Inject

class FamilyViewModel @Inject constructor(val fireBase: FirebaseAuth, val firebaseDatabase: FirebaseDatabase,val sharedPreferences: SharedPreferences) : ViewModel() {
    companion object{

    }
    var data: MutableLiveData<List<AddChild>> = getChildList()
    fun getChildList(): MutableLiveData<List<AddChild>> {
        var returnData: MutableLiveData<List<AddChild>> = MutableLiveData<List<AddChild>>()
        val db = firebaseDatabase.getReference("children")
        val childrenList = ArrayList<AddChild>()
        Log.d("TAG", "getChildList11111: " + sharedPreferences.getString("id", ""))
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                Log.d("TAG", "getChildList1231231: " + child.child("nameSurname").getValue().toString())
                //if (child.child("parentName").getValue().toString().equals(sharedPreferences.getString("id", ""))) {
                    childrenList.add(
                            AddChild(
                                    child.child("nameSurname").getValue().toString(),
                            child.child("schoolNumber").getValue().toString(),
                            child.child("service").getValue().toString().toBoolean(),
                                    child.child("parentName").getValue().toString()
                            )
                    )
                //}
            }

        }.addOnFailureListener {
        }
        Log.d("TAG", "getChildList:asdasdsada " + childrenList.size)
        returnData.value =  childrenList
        return returnData
    }
}
