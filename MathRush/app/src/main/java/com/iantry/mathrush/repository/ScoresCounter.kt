package com.iantry.mathrush.repository

/**
 * Created by Andrey on 07.01.2018.
 */
class ScoresCounter {

    var comboMultiplier = 1
    var totalScores = 0

    fun calculateScores(): Int {
        totalScores += 2 * comboMultiplier
        return totalScores
    }
}