package example.com.serviceapp.ui.service.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.ui.family.feature.addChild.AddChild
import example.com.serviceapp.utils.children
import example.com.serviceapp.utils.nameSurname
import example.com.serviceapp.utils.parentName
import example.com.serviceapp.utils.schoolNumber
import example.com.serviceapp.utils.service
import javax.inject.Inject

class MainServiceViewModel @Inject constructor(val db: FirebaseDatabase) : ViewModel() {
    var data: MutableLiveData<List<AddChild>> = getChildrenData()
    fun getChildrenData(): MutableLiveData<List<AddChild>> {
        val db = db.getReference(children)
        val listt = MutableLiveData<List<AddChild>>()
        val list = ArrayList<AddChild>()
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                list.add(
                    AddChild(
                        child.child(nameSurname).getValue().toString(),
                        child.child(schoolNumber).getValue().toString(),
                        child.child(service).getValue().toString().toBoolean(),
                        child.child(parentName).getValue().toString()
                    )
                )
            }
            listt.value = list
        }.addOnFailureListener {
        }
        return listt
    }
}
