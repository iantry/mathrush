package com.iantry.mathrush

/**
 * Created by Andrey on 07.01.2018.
 */
class ScoresCounter {

    var totalScores = 0

    fun calculateScores(bonus: Int = 1): Int {
        totalScores += 2 * bonus
        return totalScores
    }

    fun resetScores() {
        totalScores = 0
    }
}