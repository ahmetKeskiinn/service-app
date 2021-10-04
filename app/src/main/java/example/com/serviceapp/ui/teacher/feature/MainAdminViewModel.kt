package example.com.serviceapp.ui.teacher.feature

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.utils.authenticationUtils.admin.AdminRecycler
import javax.inject.Inject

class MainAdminViewModel @Inject constructor(val db: FirebaseDatabase) : ViewModel() {
    var data: MutableLiveData<List<AddChild>> = getRequestChildrenx()
    fun getRequestChildren(): MutableLiveData<List<AddChild>> {
        val db = db.getReference("addChildrenRequest")
        val listt = MutableLiveData<List<AddChild>>()
        val list = ArrayList<AddChild>()
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                Log.d("TAG", "getRequestChildrenx: ")
                list.add(
                        AddChild(
                                child.child("nameSurname").getValue().toString(),
                                child.child("schoolNumber").getValue().toString(),
                                child.child("service").getValue().toString().toBoolean(),
                                child.child("parentName").getValue().toString()
                        )
                )
            }
            listt.value = list
        }.addOnFailureListener {
        }
        return listt
    }
    fun addChildren(model: AddChild) {
        Log.d("TAG", "addChildren: ")
        val reqestDB = db.getReference("addChildrenRequest")
        val childrenDB = db.getReference("children")
        childrenDB.child(model.nameSurname.toString()).setValue(model).addOnCompleteListener { task ->
        }
        reqestDB.child(model.nameSurname.toString()).removeValue()
    }
    fun deleteChildren(model: AddChild) {
        val reqestDB = db.getReference("addChildrenRequest")
        reqestDB.child(model.nameSurname.toString()).removeValue()
    }
}
