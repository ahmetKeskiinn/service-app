package example.com.serviceapp.ui.family.feature.addChild

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import example.com.serviceapp.utils.addChildrenRequest
import example.com.serviceapp.utils.status
import example.com.serviceapp.utils.userName
import example.com.serviceapp.utils.users
import java.util.*
import javax.inject.Inject

class AddChildrenViewModel @Inject constructor(
    val fireBase: FirebaseAuth,
    val firebaseDatabase: FirebaseDatabase,
    val storageReference: StorageReference,
    val db: FirebaseFirestore
) : ViewModel() {

    fun addChild(childModel: AddChild): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        val db = firebaseDatabase.getReference(addChildrenRequest)
        childModel.parentName = fireBase.currentUser?.email.toString()
        db.child(childModel.nameSurname.toString()).setValue(childModel).addOnCompleteListener { task ->
            isSuccess.setValue(task.isSuccessful)
        }
        return isSuccess
    }

    fun setChildrenImage(filePath: Uri, studentName: String, studentNumber: String): MutableLiveData<String>{
        val isSuccess = MutableLiveData<String>()
        val ref = storageReference.child("uploads/" + UUID.randomUUID().toString())
        val uploadTask = ref.putFile(filePath)
        val urlTask =
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val data = HashMap<String, Any>()
                    val currentUser = fireBase.currentUser
                    val informations = currentUser?.email+"," + studentName+"," + studentNumber
                    data[informations] = downloadUri.toString()
                    db.collection("posts")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            isSuccess.value = task.result.toString()
                        }
                        .addOnFailureListener { e ->
                            isSuccess.value = "default"
                        }
                } else {
                    isSuccess.value = "default"
                }
            }.addOnFailureListener{
                isSuccess.value = "default"
            }
        return isSuccess
    }
}

