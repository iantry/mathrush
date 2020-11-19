package com.iantry.mathrush.repository

import android.content.Context
import android.content.SharedPreferences
import com.iantry.mathrush.booleanDelegate
import com.iantry.mathrush.intDelegate
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Andrey on 28.07.2020
 */

private const val MAIN_PREF = "mainPref"
private const val HIGH_SCORE = "highScore"
private const val HIGH_SCORE_MINUS = "highScoreMinus"
private const val HIGH_SCORE_PLUS_MINUS = "highScorePlusMinus"
private const val SOUND = "sound"


class PrefHelper(context: Context) {

    companion object {
        const val THEME_DAY = 1
        const val THEME_NIGHT = 2
       // private val prefHelper: PrefHelper by lazy { PrefHelper() }
    }


    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(MAIN_PREF, Context.MODE_PRIVATE)

    var isPlusSignOn by sharedPreferences.booleanDelegate()

    var isMinusSignOn by sharedPreferences.booleanDelegate()

    var isMultiplySignOn by sharedPreferences.booleanDelegate()

    var isDivisionSignOn by sharedPreferences.booleanDelegate()

    private var highScorePrefName = { HIGH_SCORE + isPlusSignOn + isMinusSignOn + isMultiplySignOn + isDivisionSignOn }

    var highScore by sharedPreferences.intDelegate(0) {"highScore"}  //{ it -> highScorePrefName() }

    var colorTheme by sharedPreferences.intDelegate()

    var isSoundOn by sharedPreferences.booleanDelegate()

    fun isLastSign(): Boolean {
        var counter = 0
        if (isPlusSignOn)
            counter++
        if (isMinusSignOn)
            counter++
        if (isMultiplySignOn)
            counter++
        if (isDivisionSignOn)
            counter++

        return counter == 1
    }
}

