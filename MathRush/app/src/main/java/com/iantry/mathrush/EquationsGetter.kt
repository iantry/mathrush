package com.iantry.mathrush

import android.content.Context
import kotlin.collections.ArrayList

private const val TAG = "EquationsGetter"

class EquationsGetter(private val context: Context) {

    private var correctAnswerCounter = 0
    private var equationsUsedIndex = linkedSetOf<Int>()
    private var lastIndex = -1

    fun retrieveEquation(): Pair<ArrayList<String>, Boolean> {

        var isAdded = false
        var equationIndex: Int
        val plusEquations = context.resources.getStringArray(R.array.plus_equations)
        val minusEquations = context.resources.getStringArray(R.array.minus_equations)
        val multiplyEquations = context.resources.getStringArray(R.array.multiply_equations)
        val divisionEquations = context.resources.getStringArray(R.array.division_equations)
        val commonEquations = arrayListOf<String>()
        val prefHelper = SharedPreferenceHelper(context)

        if(prefHelper.isPlusSignOn) commonEquations.addAll(plusEquations)
        if(prefHelper.isMinusSignOn) commonEquations.addAll(minusEquations)
        if(prefHelper.isMultiplySignOn) commonEquations.addAll(multiplyEquations)
        if(prefHelper.isDivisionSignOn) commonEquations.addAll(divisionEquations)
        if(commonEquations.size == 0)commonEquations.addAll(plusEquations)

        do {
            if (equationsUsedIndex.size == commonEquations.size)
                equationsUsedIndex = linkedSetOf()

            equationIndex = (commonEquations.indices).random()
            if(lastIndex == equationIndex) continue

            isAdded = equationsUsedIndex.add(equationIndex)

        } while(!isAdded)
        lastIndex = equationIndex

        return Pair(getSplittedEquation(commonEquations[equationIndex]), isCorrectEquation(commonEquations[equationIndex]))
    }

    private fun isCorrectEquation(equation: String): Boolean {

        //val equationWithoutSpace = equation.replace(" ", "")
        val equationList = split(equation)
        val equationDigitList: ArrayList<Int> = arrayListOf()
        for(i in equationList) {
            equationDigitList.add(i.toInt())
        }
        return when {
            equation.contains("+") -> equationDigitList[0] + equationDigitList[1] == equationDigitList[2]
            equation.contains("-") -> equationDigitList[0] - equationDigitList[1] == equationDigitList[2]
            equation.contains("×") -> equationDigitList[0] * equationDigitList[1] == equationDigitList[2]
            else -> equationDigitList[0] / equationDigitList[1] == equationDigitList[2]
        }
    }

    private fun getSplittedEquation(equation: String): ArrayList<String> {

        val equationList = split(equation).toMutableList()
        when {
            equation.contains("+") -> equationList.add(1, "+")
            equation.contains("-") -> equationList.add(1, "-")
            equation.contains("×") -> equationList.add(1, "×")
            equation.contains("÷") -> equationList.add(1, "÷")
        }
        return equationList as ArrayList<String>
    }

    private val split = { equation: String -> equation.split(Regex("[-+×÷=]")) }

}

