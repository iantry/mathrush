package com.iantry.mathrush.repository

import android.content.Context
import com.iantry.mathrush.Equation
import com.iantry.mathrush.rand


class DataRepository(val context: Context) {

    private val dynamicEquationGetter = DynamicEquationGetter(context)
    private val staticEquationGetter = StaticEquationGetter(context)
    private val scoreCounter = ScoresCounter()
    private val isDynamicEquation = rand()

    init {
        generateEquations()
    }

    fun generateEquations() {
        if(isDynamicEquation == 0) dynamicEquationGetter.generateEquations()
        else staticEquationGetter.generateEquations()
    }

    fun getEquation(): Equation {
        return if(isDynamicEquation == 0) dynamicEquationGetter.retrieveEquation()
        else staticEquationGetter.retrieveEquation()
    }

    //region Score
    fun calculateScore() = scoreCounter.calculateScores()

    fun resetComboMultiplier() {
        scoreCounter.comboMultiplier = 1
    }

    fun increaseComboMultiplier() {
        scoreCounter.comboMultiplier++
    }

    fun getComboMultiplier() = scoreCounter.comboMultiplier
    //endregion

    fun onTrueAnswer() {}

    fun onFalseAnswer() {
        scoreCounter.totalScores = 0
        resetComboMultiplier()
    }
}