package com.shivam_raj.circletalk.di

import com.shivam_raj.circletalk.server.Server
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Functions
import io.appwrite.services.Realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideAccount(): Account = Server.getAccountInstance()

    @Provides
    @Singleton
    fun provideDatabase(): Databases = Server.getDatabaseInstance()

    @Provides
    @Singleton
    fun provideRealtime(): Realtime = Server.getRealtimeInstance()

    @Provides
    @Singleton
    fun provideFunctions(): Functions = Server.getFunctionsInstance()

}