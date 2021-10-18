package example.com.serviceapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import example.com.serviceapp.BuildConfig
import example.com.serviceapp.R
import example.com.serviceapp.utils.IOnBackPressed

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }
    companion object {
        const val ACTION_STOP_FOREGROUND = "${BuildConfig.APPLICATION_ID}.stopforeground"
    }
}
