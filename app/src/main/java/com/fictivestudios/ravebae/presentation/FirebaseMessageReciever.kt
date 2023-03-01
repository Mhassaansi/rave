package com.fictivestudios.ravebae.presentation


import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.SplashActivity
import com.fictivestudios.ravebae.presentation.models.NotificationChatSenderModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson


class FirebaseMessageReceiver : FirebaseMessagingService() {


    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCMtoken", "OnMessageRecieved : ${remoteMessage.data}")

        // val arr: List<NotificationChatSenderModel> = List<NotificationChatSenderModel>(remoteMessage.data.get("sender_object"))

//        val gson = Gson()
//        val json:String? = gson.toJson(remoteMessage.data.get("sender_object"))


        // remoteMessage.data.keys.forEach { Log.d("keys",it)}

        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
//        if(remoteMessage.getData().size()>0){
//            showNotification(remoteMessage.getData().get("title"),
//                          remoteMessage.getData().get("message"));
//        }


//        // Second case when notification payload is
//        // received.
        if (remoteMessage.notification != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.

            Log.d(
                "FCMtoken",
                "remoteMessage.getNotification() title: ${remoteMessage.notification?.title} body: ${remoteMessage.notification?.body} notification: ${remoteMessage.notification} type: ${
                    remoteMessage.data.get(
                        "notification_type"
                    )
                }"
            )

            // "msg_notify"
            sendNotification(
                remoteMessage.notification?.title.toString(),
                remoteMessage.notification?.body.toString(),
                remoteMessage.data.get("notification_type") ?: "null",
                remoteMessage.data.get("sender_object") ?: "",
                remoteMessage.data.get("sender_id") ?: "",
                remoteMessage.data.get("sender_name") ?: "",
                remoteMessage.data.get("sender_image") ?: ""
            )
        }

//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("token", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            //val msg = getString(R.string.msg_token_fmt, token)
//            Log.d("token", "TOKEN: "+token)
//            Toast.makeText(baseContext, ""+token, Toast.LENGTH_SHORT).show()
//        })
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d("FCMtoken", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // sendRegistrationToServer(token)
    }

    // Method to get the custom Design for the display of
    // notification.
    private fun getCustomDesign(
        title: String,
        message: String
    ): RemoteViews {
        val remoteViews = RemoteViews(
            getApplicationContext().getPackageName(),
            R.layout.notification
        )
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(
            R.id.icon,
            R.drawable.app_icon
        )
        return remoteViews
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(
        title: String,
        message: String,
        type: String,
        senderChatObject: String?,
        senderId: String,
        senderName: String,
        senderImage: String
    ) {
        val notificationIntent: Intent
        if (isAppRunning()) {
            notificationIntent = Intent(this, MainActivity::class.java)
        } else {
            notificationIntent = Intent(this, SplashActivity::class.java)
        }
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        notificationIntent.putExtra("notification_type", type)
        // notificationIntent.putExtra("sender_chat", senderChatObject)
        notificationIntent.putExtra("sender_id", senderId)
        notificationIntent.putExtra("sender_name", senderName)
        notificationIntent.putExtra("sender_image", senderImage)



        val contentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

//        val contentIntent = PendingIntent.getActivity(
//            this, 0, notificationIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )

        val notificationBuilder = NotificationCompat.Builder(this, packageName)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            // .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(contentIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                packageName,
                title,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = message
                enableVibration(true)
                setShowBadge(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setAllowBubbles(true)
                }
            }
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun navigate(bundle: Bundle) {
    }

    fun isAppRunning(): Boolean {
        val services =
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
        return services.firstOrNull {
            it.processName.equals(
                "com.fictivestudios.ravebae",
                true
            )
        } != null
    }

//    // Method to display the notifications
//    fun showNotification(
//        title: String,
//        message: String
//    ) {
//        // Pass the intent to switch to the MainActivity
//        val intent = Intent(this, MainActivity::class.java)
//        // Assign channel ID
//        val channel_id = "ravebae_notification_channel"
//        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
//        // the activities present in the activity stack,
//        // on the top of the Activity that is to be launched
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        // Pass the intent to PendingIntent to start the
//        // next Activity
//        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // PendingIntent.FLAG_ONE_SHOT,
//            PendingIntent.getActivity(
//                this, 0, intent,
//                PendingIntent.FLAG_IMMUTABLE
//            )
//        } else {
//            // PendingIntent.FLAG_IMMUTABLE
//            PendingIntent.getActivity(
//                this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT
//            )
//        }
//
//        // Create a Builder object using NotificationCompat
//        // class. This will allow control over all the flags
//        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
//            getApplicationContext(),
//            channel_id
//        )
//            .setSmallIcon(R.drawable.app_icon_round)
//            .setAutoCancel(true)
//            .setVibrate(
//                longArrayOf(
//                    1000, 1000, 1000,
//                    1000, 1000
//                )
//            )
//            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
//
//        // A customized design for the notification can be
//        // set only for Android versions 4.1 and above. Thus
//        // condition for the same is checked here.
//        if (Build.VERSION.SDK_INT
//            >= Build.VERSION_CODES.JELLY_BEAN
//        ) {
//            builder = builder.setContent(
//                getCustomDesign(title, message)
//            )
//        }
////        // If Android Version is lower than Jelly Beans,
////        else {
////            builder = builder.setContentTitle(title)
////                .setContentText(message)
////                .setSmallIcon(R.drawable.gfg)
////        }
//        // Create an object of NotificationManager class to
//        // notify the
//        // user of events that happen in the background.
//        val notificationManager = getSystemService(
//            Context.NOTIFICATION_SERVICE
//        ) as NotificationManager?
//        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT
//            >= Build.VERSION_CODES.O
//        ) {
//            val notificationChannel = NotificationChannel(
//                // web_app
//                channel_id, ""+message,
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager!!.createNotificationChannel(
//                notificationChannel
//            )
//        }
//        notificationManager!!.notify(0, builder.build())
//    }
}