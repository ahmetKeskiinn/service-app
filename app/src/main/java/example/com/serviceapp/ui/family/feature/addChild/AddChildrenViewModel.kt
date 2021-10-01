package example.com.serviceapp.ui.family.feature.addChild

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class AddChildrenViewModel @Inject constructor(val fireBase: FirebaseAuth ,val firebaseDatabase: FirebaseDatabase) : ViewModel(){
    fun addChild(childModel: AddChild){
        val db = firebaseDatabase.getReference("children")
        childModel.parentName = fireBase.currentUser?.email.toString()
        db.child(childModel.nameSurname.toString()).setValue(childModel).addOnSuccessListener {
            Log.d("TAG", "addChild: ")
        }
    }
}
