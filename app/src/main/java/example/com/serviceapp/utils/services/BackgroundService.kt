package example.com.serviceapp.utils.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.utils.backgroundDelay
import example.com.serviceapp.utils.serviceLocation
import javax.inject.Inject

class BackgroundService : Service() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MyApp.appComponent.inject(this)
        mHandler = Handler()
        mRunnable = Runnable { obtieneLocalizacion() }
        mHandler.postDelayed(mRunnable, backgroundDelay)
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun obtieneLocalizacion() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val db = firebaseDatabase.getReference(serviceLocation)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                db.child(serviceLocation).setValue(ServiceLocationModel(location?.latitude.toString(), location?.altitude.toString())).addOnCompleteListener { task ->
                    Toast.makeText(applicationContext, "Lokasyon kaydedildi!", Toast.LENGTH_SHORT).show()
                }
            }
        mHandler.postDelayed(mRunnable, backgroundDelay)
    }
}
