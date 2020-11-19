package com.iantry.mathrush.di.appcomponent

import com.iantry.mathrush.App
import com.iantry.mathrush.di.mainactivitycomponent.MainActivityComponent
import com.iantry.mathrush.di.mainactivitycomponent.MainActivityModule
import com.iantry.mathrush.viewmodel.EquationViewModel
import com.iantry.mathrush.viewmodel.SettingViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {

    fun injectTo(target: App)
    fun injectTo(target: EquationViewModel)
    fun injectTo(target: SettingViewModel)

    fun mainActivityComponent(mainActivityModule: MainActivityModule): MainActivityComponent
}