package com.shivam_raj.circletalk.notification

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shivam_raj.circletalk.R
import com.shivam_raj.circletalk.server.Server
import com.shivam_raj.circletalk.storage.FCMTokenManager
import io.appwrite.ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            FCMTokenManager.storeToken(applicationContext, token)
            try {
                val account = Server.getAccountInstance()
                FCMTokenManager.getTargetId(applicationContext).collectLatest {
                    if (it == null) {
                        val target = account.createPushTarget(ID.unique(), token)
                        FCMTokenManager.storeTarget(applicationContext, target.id)
                    } else {
                        account.updatePushTarget(it, token)
                    }
                }
            } catch (e: Exception) {
                Log.d("TAG", "onNewToken: ${e.message}")
            }

        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("Data", "onMessageReceived: ${message.data}  ${message.messageType}")
        message.notification?.let { message ->
            sendNotification(applicationContext, message)
        }
    }
}

private fun sendNotification(context: Context, message: RemoteMessage.Notification) {
    val notification = NotificationCompat.Builder(context, NotificationConstants.MESSAGE_CHANNEL_ID)
        .setContentTitle(message.title)
        .setContentText(message.body)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.notify(1, notification.build())
}