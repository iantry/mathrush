package com.iantry.mathrush.di.modules

import android.app.Application
import android.content.Context
import com.iantry.mathrush.repository.DataRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideDataRepository(context: Application) : DataRepository = DataRepository(context)

}