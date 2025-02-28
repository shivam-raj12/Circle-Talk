package com.shivam_raj.circletalk.server

import android.content.Context
import io.appwrite.Client
import io.appwrite.models.User
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Functions
import io.appwrite.services.Realtime

object Server {
    @Volatile
    private lateinit var client: Client
    private var user: User<Map<String, Any>>? = null
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

    fun initializeUser(user: User<Map<String, Any>>?) {
        this.user = user
    }

    fun getUser() = user

    fun getAccountInstance() = Account(client)

    fun getDatabaseInstance() = Databases(client)

    fun getRealtimeInstance() = Realtime(client)

    fun getFunctionsInstance() = Functions(client)
}