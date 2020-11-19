package com.iantry.mathrush.di.modules

import com.iantry.mathrush.di.MainActivityScope
import com.iantry.mathrush.ui.MainActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class MainActivityModule(private val mainActivity: MainActivity) {

    @Provides
    fun provideMainActivity(): MainActivity = mainActivity
}