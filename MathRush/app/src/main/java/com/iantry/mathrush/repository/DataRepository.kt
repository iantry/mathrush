package com.iantry.mathrush.repository

import android.content.Context
import com.iantry.mathrush.Equation
import com.iantry.mathrush.rand


class DataRepository(val context: Context,
                     private val equationGetter: EquationGetter,
                     private val scoresCounter: ScoresCounter) {


    init {
        generateEquations()
    }

    fun generateEquations() {
       equationGetter.generateEquations()
    }

    fun getEquation(): Equation = equationGetter.retrieveEquation()

    //region Score
    fun calculateScore() = scoresCounter.calculateScores()

    fun resetComboMultiplier() {
        scoresCounter.comboMultiplier = 1
    }

    fun increaseComboMultiplier() {
        scoresCounter.comboMultiplier++
    }

    fun getComboMultiplier() = scoresCounter.comboMultiplier
    //endregion

    fun onTrueAnswer() {}

    fun onFalseAnswer() {
        scoresCounter.totalScores = 0
        resetComboMultiplier()
    }
}