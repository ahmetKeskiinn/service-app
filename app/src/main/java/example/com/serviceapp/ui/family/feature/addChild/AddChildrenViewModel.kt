package example.com.serviceapp.ui.family.feature.addChild

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.utils.addChildrenRequest
import javax.inject.Inject

class AddChildrenViewModel @Inject constructor(val fireBase: FirebaseAuth, val firebaseDatabase: FirebaseDatabase) : ViewModel() {
    fun addChild(childModel: AddChild): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        val db = firebaseDatabase.getReference(addChildrenRequest)
        childModel.parentName = fireBase.currentUser?.email.toString()
        db.child(childModel.nameSurname.toString()).setValue(childModel).addOnCompleteListener { task ->
            isSuccess.setValue(task.isSuccessful)
        }
        return isSuccess
    }
}
