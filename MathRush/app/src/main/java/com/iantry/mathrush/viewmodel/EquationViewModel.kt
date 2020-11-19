package com.iantry.mathrush.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.iantry.mathrush.Equation
import com.iantry.mathrush.app
import com.iantry.mathrush.repository.DataRepository
import com.iantry.mathrush.ui.UserEvents
import javax.inject.Inject

class EquationViewModel(application: Application): AndroidViewModel(application), UserEvents {

    @Inject
    lateinit var dataRepository: DataRepository

    val equationLd = MutableLiveData<Equation>()
    val scoreLd = MutableLiveData<Int>()

    val isCorrectAnswerLd = MutableLiveData<Event<Pair<Boolean, Int>>>()
    var isUntilEquationHalfTime = true

    init {
        application.app.appComponent.injectTo(this)
        generateNewEquations()
        requestNewEquation()
        setScore()
    }

    //region Equation
    fun generateNewEquations() = dataRepository.generateEquations()

    fun requestNewEquation() {
        equationLd.value = dataRepository.getEquation()
    }
    //endregion
    //region Score
    private fun calculateScore() {
        scoreLd.value = dataRepository.calculateScore()
    }

    private fun setScore(v: Int = 0) {
        scoreLd.value = v
    }

    fun getComboMultiplier() = dataRepository.getComboMultiplier()

    private fun increaseComboMultiplier() = dataRepository.increaseComboMultiplier()

    private fun resetComboMultiplier() {
        dataRepository.resetComboMultiplier()
    }
    //endregion

    private fun onTrueAnswer() {
        if(!isUntilEquationHalfTime)
            resetComboMultiplier()

        isCorrectAnswerLd.value = Event(Pair(true, dataRepository.getComboMultiplier()))
        calculateScore()

        if(isUntilEquationHalfTime)
            increaseComboMultiplier()

        dataRepository.onTrueAnswer()
    }

    private fun onFalseAnswer() {

        isCorrectAnswerLd.value = Event(Pair(false, 1))
        dataRepository.onFalseAnswer()
    }

    //region UserEvents
    override fun onCorrectBtnClick() {
        if (equationLd.value?.isCorrect != false) onTrueAnswer()
        else onFalseAnswer()
    }

    override fun onIncorrectBtnClick() {
        if (equationLd.value?.isCorrect != false) onFalseAnswer()
        else onTrueAnswer()
    }

    override fun onTimeOver() {
        onFalseAnswer()
    }

    override fun onRestartBtnClick() {
        prepareData()
    }
    //endregion

    private fun prepareData() {
        requestNewEquation()
        resetComboMultiplier()
        setScore()
    }

    override fun onCleared() {
        super.onCleared()
    }
}