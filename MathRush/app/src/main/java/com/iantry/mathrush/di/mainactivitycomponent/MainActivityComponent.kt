package com.iantry.mathrush.di.mainactivitycomponent

import com.iantry.mathrush.ui.MainActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [MainActivityModule::class]
)
interface MainActivityComponent {

    fun injectTo(target: MainActivity)
}

