package com.iantry.mathrush.repository

import android.content.Context
import com.iantry.mathrush.Equation
import com.iantry.mathrush.positive
import com.iantry.mathrush.rand
import kotlin.collections.ArrayList

private const val TAG = "DynamicEquationGetter"

class DynamicEquationGetter(private val context: Context, private val prefHelper: PrefHelper) : EquationGetter {

    private var equationsUsedIndex = linkedSetOf<Int>()
    private var lastIndex = -1
    private lateinit var commonEquations: ArrayList<Equation>


    override fun generateEquations() {

        commonEquations = arrayListOf()
      //  val prefHelper = PrefHelper.instance(context)

        if(prefHelper.isPlusSignOn) commonEquations.plusAssign(generateEquation(sign = "+"))
        if(prefHelper.isMinusSignOn) commonEquations.plusAssign(generateEquation(0, 20, "-"))
        if(prefHelper.isMultiplySignOn) commonEquations.plusAssign(generateEquation(sign = "×"))
        if(prefHelper.isDivisionSignOn) commonEquations.plusAssign(generateEquation(1, 9, "÷"))
        if(commonEquations.size == 0) commonEquations.plusAssign(generateEquation(sign = "+"))
    }

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

        return commonEquations[equationIndex]
    }

    private fun generateEquation(from: Int = 1, to: Int = 9, sign: String): ArrayList<Equation> {
        val equationList = mutableListOf<Equation>()

        val incorrectAnswer = { startValue: Int, include: Int, correctAnswer: Int ->
            if(rand() == 0)
                positive(correctAnswer - rand(startValue, include))
            else
                correctAnswer + rand(startValue, include)
        }

        for (i in from..to) {
            loop@ for(j in 0..9) {
                when(sign) {
                    "+" -> {
                        val correctAnswer = i + j
                        equationList.add(Equation(i, j, correctAnswer, sign, correctAnswer))
                        equationList.add(Equation(i, j, incorrectAnswer(1, 2, correctAnswer), sign, correctAnswer))
                    }
                    "-" -> {
                        val correctAnswer = i - j
                        if(correctAnswer < 0 || i == 0) continue@loop
                        equationList.add(Equation(i, j, correctAnswer, sign, correctAnswer))
                        equationList.add(Equation(i, j, incorrectAnswer(1, 2, correctAnswer), sign, correctAnswer))
                    }
                    "×" -> {
                        val correctAnswer = i * j
                        equationList.add(Equation(i, j, correctAnswer, sign, correctAnswer))
                        equationList.add(Equation(i, j, incorrectAnswer(1, 3, correctAnswer), sign, correctAnswer))
                    }
                    "÷" -> {
                        if(j == 0) continue@loop
                        val multiplier = i * j
                        val correctAnswer = multiplier / j
                        equationList.add(Equation(multiplier, j, correctAnswer, sign, correctAnswer))
                        equationList.add(Equation(multiplier, j, incorrectAnswer(1, 2, correctAnswer), sign, correctAnswer))
                    }
                }
            }
        }
        return equationList as ArrayList<Equation>
    }

}

