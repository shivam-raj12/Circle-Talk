package com.shivam_raj.circletalk

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import com.google.firebase.FirebaseApp
import com.shivam_raj.circletalk.notification.NotificationConstants
import com.shivam_raj.circletalk.server.Server
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Server.initializeClient(this)
        initializeNotification(this)
    }
}

private fun initializeNotification(context: Context){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            NotificationConstants.MESSAGE_CHANNEL_ID,
            NotificationConstants.MESSAGE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = NotificationConstants.MESSAGE_CHANNEL_DESCRIPTION
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}