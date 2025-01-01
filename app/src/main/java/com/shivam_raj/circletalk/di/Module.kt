package com.shivam_raj.circletalk.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideClient(@ApplicationContext context: Context): Client {
        return Client(context)
            .setEndpoint("https://cloud.appwrite.io/v1")
            .setProject("676f6c780038d69689d8")
    }


}