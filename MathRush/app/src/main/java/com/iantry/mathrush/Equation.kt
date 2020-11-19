package com.iantry.mathrush

data class Equation(val value1: Int,
               val value2: Int,
               val answer: Int,
               val sign: String,
               val correctAnswer: Int) {

    val isCorrect = answer == correctAnswer

    override fun toString(): String {
        return "$value1$sign$value2=$answer"
    }
}
