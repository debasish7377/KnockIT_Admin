package com.example.knockitadminapp.Activity

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import com.example.knockitadminapp.Activity.DeliveryActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URL
import java.util.Timer
import javax.annotation.Nullable


internal class MyServices : Service() {
    var alarmHour: Long? = null
    var alarmMinute: Long? = null
    private val t = Timer()
    var timeStamp: Long? = null
    lateinit var productTitle: String
    lateinit var productPrice: String
    lateinit var productId: String

    companion object {
        private const val CHANNEL_ID = "MyNotificationChannelID"
        private const val CHANNEL_ID_2 = "MyNotification"
        private const val NOTIFICATION_ID = 100
        lateinit var ringtone: Ringtone
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        alarmHour = intent.getLongExtra("alarmHour", 0);
//        alarmMinute = intent.getLongExtra("alarmMinute", 0);
        FirebaseFirestore.getInstance()
            .collection("OrderNotification")
            .document(FirebaseAuth.getInstance().uid.toString())
            .addSnapshotListener { value, error ->
                timeStamp = value?.getLong("timeStamp")
                productTitle = value?.getString("productTitle").toString()
                productPrice = value?.getString("productPrice").toString()
                productId = value?.getString("productId").toString()

                FirebaseFirestore.getInstance()
                    .collection("PRODUCTS")
                    .document(productId.toString())
                    .addSnapshotListener { value, error ->
                        var productImage = value?.getString("productImage").toString()

                        val time: Thread = object : Thread() {
                            override fun run() {
                                try {
                                    sleep(3000)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                } finally {

                                    try {

                                        val notification: Notification =
                                            NotificationCompat.Builder(
                                                applicationContext,
                                                CHANNEL_ID
                                            )
//                                        .setContentTitle("My Alarm clock")
//                                        .setContentText("Alarm time – ")
//                                        .setSmallIcon(R.drawable.sym_def_app_icon)
//                                        .setContentIntent(pendingIntent)
                                                .build()
                                        startForeground(1, notification)
                                        var notificationChannel: NotificationChannel? = null
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            notificationChannel = NotificationChannel(
                                                CHANNEL_ID,
                                                "My Alarm clock Service",
                                                NotificationManager.IMPORTANCE_DEFAULT
                                            )
                                        }
                                        val notificationManager = getSystemService(
                                            NotificationManager::class.java
                                        )
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            notificationManager.createNotificationChannel(
                                                notificationChannel!!
                                            )
                                        }
                                    } catch (e: java.lang.Exception) {
                                        e.printStackTrace()
                                    }

                                    ringtone = RingtoneManager.getRingtone(
                                        getApplicationContext(),
                                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                                    )
                                    try {
                                        if (System.currentTimeMillis() >= timeStamp!!
                                        ) {

                                            val drawable = ResourcesCompat.getDrawable(
                                                getResources(),
                                                R.drawable.star_on,
                                                null
                                            )
                                            val bitmapDrawable = drawable as BitmapDrawable?
                                            val largeIcon = bitmapDrawable!!.bitmap

                                            val notificationIntent =
                                                Intent(
                                                    applicationContext,
                                                    DeliveryActivity::class.java
                                                )
                                            val pendingIntent = PendingIntent.getActivity(
                                                applicationContext,
                                                0,
                                                notificationIntent,
                                                PendingIntent.FLAG_IMMUTABLE
                                            )

                                            var url = URL(productImage)
                                            var image = BitmapFactory . decodeStream (url.openConnection()
                                                .getInputStream());

                                            val notificationManager = getSystemService(
                                                NOTIFICATION_SERVICE
                                            ) as NotificationManager
                                            var notification: Notification =
                                                NotificationCompat.Builder(getApplicationContext())
                                                    .setLargeIcon(image)
                                                    .setSmallIcon(R.drawable.star_on)
                                                    .setContentTitle("₹" + productPrice)
                                                    .setSubText("Your Order")
                                                    .setContentText(productTitle)
                                                    .setChannelId(CHANNEL_ID_2)
                                                    .setContentIntent(pendingIntent)
                                                    .build()
                                            startForeground(2, notification)
                                            ringtone?.play()
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                notificationManager!!.createNotificationChannel(
                                                    NotificationChannel(
                                                        CHANNEL_ID_2,
                                                        "New Notification",
                                                        NotificationManager.IMPORTANCE_HIGH
                                                    )
                                                )
                                            }
//                                            notificationManager!!.notify(
//                                                NOTIFICATION_ID,
//                                                notification
//                                            )


                                        } else {
                                            ringtone?.stop()
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                        time.start()
                    }
            }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        try {
            ringtone!!.stop()
        }catch (e: Exception){
            e.printStackTrace()
        }
        t.cancel()
        super.onDestroy()
    }
}