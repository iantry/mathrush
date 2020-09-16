package com.iantry.mathrush.repository

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Andrey on 28.07.2020
 */

private const val MAIN_PREF = "mainPref"
private const val HIGH_SCORE = "highScore"
private const val HIGH_SCORE_MINUS = "highScoreMinus"
private const val HIGH_SCORE_PLUS_MINUS = "highScorePlusMinus"
private const val SOUND = "sound"
private const val COLOR_THEME = "colorTheme"
private const val PLUS_EQUATION = "plusEquation"
private const val MINUS_EQUATION = "minusEquation"
private const val MULTIPLY_EQUATION = "multiplyEquation"
private const val DIVISION_EQUATION = "divisionEquation"
private const val GAME_MODE = "gameMode"


class PrefHelper private constructor(){

    companion object {
        const val THEME_DAY = 1
        const val THEME_NIGHT = 2
        lateinit var prefHelper: PrefHelper
        private lateinit var context: Context

        /**
         ** For appContext need use application level context only
         **/
        fun instance(appContext: Context): PrefHelper {
            return if(!Companion::prefHelper.isInitialized) {
                context = appContext
                prefHelper = PrefHelper()
                prefHelper
            }
            else prefHelper
        }
    }


    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(MAIN_PREF, Context.MODE_PRIVATE)


    var isPlusSignOn = sharedPreferences.getBoolean(PLUS_EQUATION, true)
        set(isOn) {
            if (field != isOn) {
                field = isOn
                sharedPreferences.edit().putBoolean(PLUS_EQUATION, isOn).apply()
            }
        }

    var isMinusSignOn = sharedPreferences.getBoolean(MINUS_EQUATION, true)
        set(isOn) {
            if (field != isOn) {
                field = isOn
                sharedPreferences.edit().putBoolean(MINUS_EQUATION, isOn).apply()
            }
        }

    var isMultiplySignOn = sharedPreferences.getBoolean(MULTIPLY_EQUATION, false)
        set(isOn) {
            if (field != isOn) {
                field = isOn
                sharedPreferences.edit().putBoolean(MULTIPLY_EQUATION, isOn).apply()
            }
        }

    var isDivisionSignOn = sharedPreferences.getBoolean(DIVISION_EQUATION, false)
        set(isOn) {
            if (field != isOn) {
                field = isOn
                sharedPreferences.edit().putBoolean(DIVISION_EQUATION, isOn).apply()
            }
        }

    private var highScorePrefName =
        { HIGH_SCORE + isPlusSignOn + isMinusSignOn + isMultiplySignOn + isDivisionSignOn }

    var highScore
        get() = sharedPreferences.getInt(highScorePrefName(), 0)
        set(highScore) {
            sharedPreferences.edit().putInt(highScorePrefName(), highScore).apply()
        }


    var colorTheme
        get() = sharedPreferences.getInt(COLOR_THEME, THEME_DAY)
        set(colorTheme) {
            sharedPreferences.edit().putInt(COLOR_THEME, colorTheme).apply()
        }

    var isSoundOn
        get() = sharedPreferences.getBoolean(SOUND, true)
        set(isOn) {
            sharedPreferences.edit().putBoolean(SOUND, isOn).apply()
        }

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

    fun setNewSign(signType: Int, isOn: Boolean) {
        when (signType) {
            1 -> isPlusSignOn = isOn
            2 -> isMinusSignOn = isOn
            3 -> isMultiplySignOn = isOn
            4 -> isDivisionSignOn = isOn
        }
    }
}