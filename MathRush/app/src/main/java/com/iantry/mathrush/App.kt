package com.iantry.mathrush

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.iantry.mathrush.di.components.AppComponent
import com.iantry.mathrush.di.modules.AppModule
import com.iantry.mathrush.di.components.DaggerAppComponent
import com.iantry.mathrush.di.components.MainActivityComponent
import com.iantry.mathrush.di.modules.MainActivityModule
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