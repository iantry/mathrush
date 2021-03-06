package com.iantry.mathrush

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.iantry.mathrush.di.appcomponent.AppComponent
import com.iantry.mathrush.di.appcomponent.AppModule
import com.iantry.mathrush.di.appcomponent.DaggerAppComponent
import com.iantry.mathrush.di.mainactivitycomponent.MainActivityComponent
import com.iantry.mathrush.di.mainactivitycomponent.MainActivityModule
import com.iantry.mathrush.repository.DataRepository
import com.iantry.mathrush.ui.MainActivity
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent
    @Inject
    lateinit var dataRepository: DataRepository

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
        appComponent = initDagger()
        appComponent.injectTo(this)
    }

    private fun initDagger(): AppComponent {
      return DaggerAppComponent.builder()
            .appModule(AppModule(this))
          .build()
    }

    fun initMainActivityComponent(mainActivity: MainActivity): MainActivityComponent {
        return appComponent.mainActivityComponent(MainActivityModule(mainActivity))
    }


}