package com.iantry.mathrush

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.iantry.mathrush.repository.DataRepository
import com.iantry.mathrush.repository.DynamicEquationGetter

class App : Application() {

    lateinit var dataRepository: DataRepository
    private set

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
        dataRepository = DataRepository(this)
    }
}