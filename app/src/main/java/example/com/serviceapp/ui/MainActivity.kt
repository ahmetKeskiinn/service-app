package example.com.serviceapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.R
import example.com.serviceapp.ui.login.User

class MainActivity : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
      /*  db = FirebaseDatabase.getInstance().getReference("users")
        val model = User("ahmet","keskin")

        db.child("ahmet").setValue(model).addOnSuccessListener {
            Log.d("TAG", "onCreate:xx ")
        }
        db.child("ahmet").get(model).addOnSuccessListener {
            Log.d("TAG", "onCreate:xx ")
        }*/
    }
}
