package com.iantry.mathrush.repository

import android.content.Context
import com.iantry.mathrush.Equation
import com.iantry.mathrush.R

class StaticEquationGetter(private val context: Context, private val prefHelper: PrefHelper) : EquationGetter {

    private var equationsUsedIndex = linkedSetOf<Int>()
    private var lastIndex = -1
    private lateinit var commonEquations: ArrayList<String>

    override fun generateEquations() {

        commonEquations = arrayListOf()
       // val prefHelper = PrefHelper.instance(context)

        if(prefHelper.isPlusSignOn) commonEquations.addAll(getPlusEquation())
        if(prefHelper.isMinusSignOn) commonEquations.addAll(getMinusEquation())
        if(prefHelper.isMultiplySignOn) commonEquations.addAll(getMultiplyEquation())
        if(prefHelper.isDivisionSignOn) commonEquations.addAll(getDivisionEquation())
        if(commonEquations.size == 0) commonEquations.addAll(getPlusEquation())
    }

    private fun getPlusEquation() = context.resources.getStringArray(R.array.plus_equations)
    private fun getMinusEquation() = context.resources.getStringArray(R.array.minus_equations)
    private fun getMultiplyEquation() = context.resources.getStringArray(R.array.multiply_equations)
    private fun getDivisionEquation() = context.resources.getStringArray(R.array.division_equations)

    override fun retrieveEquation(): Equation {

        var isAdded = false
        var equationIndex: Int

        do {
            if (equationsUsedIndex.size == commonEquations.size)
                equationsUsedIndex = linkedSetOf()

            equationIndex = (commonEquations.indices).random()
            if(lastIndex == equationIndex) continue

            isAdded = equationsUsedIndex.add(equationIndex)

        } while(!isAdded)
        lastIndex = equationIndex

        return getSplittedEquation(commonEquations[equationIndex])
    }

    private fun getSplittedEquation(strEquation: String): Equation {

        val equationList = split(strEquation)

        when {
            strEquation.contains("+") -> equationList.add(1, "+")
            strEquation.contains("-") -> equationList.add(1, "-")
            strEquation.contains("×") -> equationList.add(1, "×")
            strEquation.contains("÷") -> equationList.add(1, "÷")
        }

        return Equation(
            value1 = equationList[0].toInt(),
            sign = equationList[1],
            value2 = equationList[2].toInt(),
            answer = equationList[3].toInt(),
            correctAnswer = getCorrectAnswer(equationList))
    }

    private val split = { equation: String -> equation.split(Regex("[-+×÷=]")).toMutableList() }

    private fun getCorrectAnswer(equationList: MutableList<String>): Int {

        val equationDigitList: MutableList<Int> = arrayListOf()

        for(i in equationList) {
            if(i != "+" && i != "-" && i != "×" && i != "÷")
                equationDigitList.add(i.toInt())
        }
        return when(equationList[1]) {
            "+" -> equationDigitList[0] + equationDigitList[1]
            "-" -> equationDigitList[0] - equationDigitList[1]
            "×" -> equationDigitList[0] * equationDigitList[1]
            else -> equationDigitList[0] / equationDigitList[1]
        }
    }
}