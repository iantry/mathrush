package com.iantry.mathrush.di.appcomponent

import android.app.Application
import android.content.Context
import com.iantry.mathrush.rand
import com.iantry.mathrush.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideEquationGetter(context: Application, prefHelper: PrefHelper): EquationGetter {

        val isDynamicEquation = rand()
        return if(isDynamicEquation == 0) DynamicEquationGetter(context, prefHelper)
        else StaticEquationGetter(context, prefHelper)
    }

    @Provides
    @Singleton
    fun providePrefHelper(context: Application): PrefHelper = PrefHelper(context)

    @Provides
    @Singleton
    fun provideScoreCounter(): ScoresCounter = ScoresCounter()

    @Provides
    @Singleton
    fun provideDataRepository(context: Application,
                              equationGetter: EquationGetter,
                              scoresCounter: ScoresCounter) : DataRepository = DataRepository(context, equationGetter, scoresCounter)

}