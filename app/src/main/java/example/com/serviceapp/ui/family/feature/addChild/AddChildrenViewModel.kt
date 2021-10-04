package example.com.serviceapp.ui.family.feature.addChild

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.utils.AuthenticationUtils.Admin.AddChildren
import javax.inject.Inject

class AddChildrenViewModel @Inject constructor(val fireBase: FirebaseAuth, val firebaseDatabase: FirebaseDatabase) : ViewModel() {
    fun addChild(childModel: AddChild, listener: AddChildren) {
        val db = firebaseDatabase.getReference("addChildrenRequest")
        childModel.parentName = fireBase.currentUser?.email.toString()
        db.child(childModel.nameSurname.toString()).setValue(childModel).addOnCompleteListener { task ->
            listener.isSuccess(task.isSuccessful)
        }
    }
}
