package com.iantry.mathrush.di.modules

import com.iantry.mathrush.di.MainActivityScope
import com.iantry.mathrush.ui.MainActivity
import com.iantry.mathrush.ui.SoundEffects
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SoundEffectsModule {

    @Provides
    fun provideSoundEffects(mainActivity: MainActivity): SoundEffects = SoundEffects(mainActivity)
}