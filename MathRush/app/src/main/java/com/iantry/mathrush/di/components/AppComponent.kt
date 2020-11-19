package com.iantry.mathrush.di.components

import com.iantry.mathrush.App
import com.iantry.mathrush.di.modules.AppModule
import com.iantry.mathrush.di.modules.DataModule
import com.iantry.mathrush.di.modules.MainActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {

    fun injectTo(target: App)

    fun mainActivityComponent(mainActivityModule: MainActivityModule): MainActivityComponent
}