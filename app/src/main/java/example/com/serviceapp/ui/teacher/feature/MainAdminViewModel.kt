package example.com.serviceapp.ui.teacher.feature

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.utils.AuthenticationUtils.Admin.AdminRecycler
import javax.inject.Inject

class MainAdminViewModel @Inject constructor(val db: FirebaseDatabase) : ViewModel() {

    fun getRequestChildren(listener: AdminRecycler) {
        val db = db.getReference("addChildrenRequest")
        var list = ArrayList<AddChild>()
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                list.add(
                    AddChild(
                        child.child("nameSurname").getValue().toString(),
                        child.child("schoolNumber").getValue().toString(),
                        child.child("service").getValue().toString().toBoolean(),
                        child.child("parentName").getValue().toString()
                    )
                )
            }
            listener.childList(list)
        }.addOnFailureListener {
        }
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
