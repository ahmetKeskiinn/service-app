package example.com.serviceapp.utils.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.database.FirebaseDatabase
import example.com.serviceapp.R
import example.com.serviceapp.di.MyApp
import example.com.serviceapp.ui.MainActivity
import example.com.serviceapp.ui.MainActivity.Companion.ACTION_STOP_FOREGROUND
import example.com.serviceapp.utils.altitude
import example.com.serviceapp.utils.latitude
import example.com.serviceapp.utils.serviceLocation
import javax.inject.Inject

class ForegroundService : Service() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MyApp.appComponent.inject(this)
        mHandler = Handler()
        mRunnable = Runnable {
            if (
                intent?.action != null &&
                intent.action.equals(ACTION_STOP_FOREGROUND, ignoreCase = true)
            ) {
                stopForeground(true)
                stopSelf()
            }
            checkLocation()
        }
        mHandler.postDelayed(mRunnable, 5000)

        return START_STICKY
    }
    private fun checkLocation() {
        val db = firebaseDatabase.getReference(serviceLocation)
        db.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                if (!child.getValue().toString().isEmpty() && !child.getValue().toString().equals("null")
                ) {
                    generateForegroundNotification(
                        child.child(latitude).getValue().toString(),
                        child.child(altitude).getValue().toString()
                    )
                } else {
                    Log.d("TAG", "checkLocation:----- ")
                }
            }
        }.addOnFailureListener {
        }
        mHandler.postDelayed(mRunnable, 5000)
    }

    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123

    private fun generateForegroundNotification(latitude: String, attitude: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intentMainLanding = Intent(this, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intentMainLanding, 0)
            iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            if (mNotificationManager == null) {
                mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert(mNotificationManager != null)
                mNotificationManager?.createNotificationChannelGroup(
                    NotificationChannelGroup(
                        "chats_group",
                        "Chats"
                    )
                )
                val notificationChannel =
                    NotificationChannel(
                        "service_channel",
                        "Service Notifications",
                        NotificationManager.IMPORTANCE_MIN
                    )
                notificationChannel.enableLights(false)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, "service_channel")

            builder.setContentTitle(
                StringBuilder(resources.getString(R.string.serviceMoving))
                    .append("\n").append(resources.getString(R.string.latitude)).append(latitude)
                    .append("\n").append(resources.getString(R.string.attitude)).append(attitude).toString()
            )
                .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("service is running").toString())
                .setContentText(resources.getString(R.string.touchToOpen))
                .setSmallIcon(R.drawable.ic_location)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
            if (iconNotification != null) {
                builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification!!, 128, 128, false))
            }
            builder.color = resources.getColor(R.color.purple_200)
            notification = builder.build()
            startForeground(mNotificationId, notification)
        }
        mHandler.postDelayed(mRunnable, 5000)
    }
}
