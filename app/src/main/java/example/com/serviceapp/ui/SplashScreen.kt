package example.com.serviceapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import example.com.serviceapp.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        Handler().postDelayed({ this.goActivity() }, 500)
    }

    fun goActivity() {
        val goMain = Intent(this@SplashScreen, MainActivity::class.java)
        this@SplashScreen.startActivity(goMain)
        finish()
    }
}
