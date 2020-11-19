package com.iantry.mathrush.di.mainactivitycomponent

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.iantry.mathrush.di.MainActivityScope
import com.iantry.mathrush.ui.MainActivity
import com.iantry.mathrush.ui.SoundEffects
import com.iantry.mathrush.viewmodel.EquationViewModel
import com.iantry.mathrush.viewmodel.SettingViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class MainActivityModule(private val mainActivity: MainActivity) {

    @Provides
    fun provideMainActivity(): MainActivity = mainActivity

    @Provides
    fun provideSoundEffects(): SoundEffects = SoundEffects(mainActivity)

    @Provides
    fun provideEquationViewModel(app: Application): EquationViewModel {
        return ViewModelProvider(mainActivity, ViewModelProvider.AndroidViewModelFactory(app))
            .get(EquationViewModel::class.java)
    }

    @Provides
    fun provideSettingViewModel(app: Application): SettingViewModel {
        return ViewModelProvider(mainActivity, ViewModelProvider.AndroidViewModelFactory(app))
                .get(SettingViewModel::class.java)
    }
}