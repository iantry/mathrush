package com.iantry.mathrush.di.appcomponent

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Application = app
}