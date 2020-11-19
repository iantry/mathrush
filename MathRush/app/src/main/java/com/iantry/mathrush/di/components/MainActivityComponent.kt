package com.iantry.mathrush.di.components

import com.iantry.mathrush.di.MainActivityScope
import com.iantry.mathrush.di.modules.MainActivityModule
import com.iantry.mathrush.di.modules.SoundEffectsModule
import com.iantry.mathrush.di.modules.ViewModelModule
import com.iantry.mathrush.ui.MainActivity
import dagger.Subcomponent
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

@Subcomponent(
    modules = [MainActivityModule::class,
        SoundEffectsModule::class,
        ViewModelModule::class]
)
interface MainActivityComponent {

    fun injectTo(target: MainActivity)
}

