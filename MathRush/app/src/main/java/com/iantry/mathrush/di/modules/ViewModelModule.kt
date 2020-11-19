package com.iantry.mathrush.di.modules

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.iantry.mathrush.App
import com.iantry.mathrush.di.MainActivityScope
import com.iantry.mathrush.ui.MainActivity
import com.iantry.mathrush.viewmodel.EquationViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    fun provideEquationViewModel(mainActivity: MainActivity, app: Application): EquationViewModel {

       return ViewModelProvider(mainActivity,
            ViewModelProvider.AndroidViewModelFactory(app))
            .get(EquationViewModel::class.java)
    }
}