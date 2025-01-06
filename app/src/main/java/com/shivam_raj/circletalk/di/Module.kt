package com.shivam_raj.circletalk.di

import com.shivam_raj.circletalk.server.Server
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.appwrite.services.Account
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideAccount(): Account = Server.getAccountInstance()

}