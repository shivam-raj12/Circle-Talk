package com.shivam_raj.circletalk.server

import android.content.Context
import io.appwrite.Client
import io.appwrite.services.Account

object Server {
    @Volatile
    private lateinit var client: Client
    private val LOCK = Any()

    fun initializeClient(context: Context) {
        synchronized(LOCK) {
            if (!::client.isInitialized) {
                client = Client(context.applicationContext)
                    .setEndpoint("https://cloud.appwrite.io/v1")
                    .setProject("676f6c780038d69689d8")
                    .setSelfSigned(true)
            }
        }
    }

    fun getAccountInstance() = Account(client)

}