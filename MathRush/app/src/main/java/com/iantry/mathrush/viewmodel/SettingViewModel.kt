package com.iantry.mathrush.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iantry.mathrush.app
import com.iantry.mathrush.repository.PrefHelper
import com.iantry.mathrush.ui.SettingsEvents
import javax.inject.Inject

class SettingViewModel(application: Application): AndroidViewModel(application), SettingsEvents {

    @Inject
    lateinit var prefHelper: PrefHelper  //PrefHelper.instance(application.applicationContext)

    private val mutableSignPlusLd = MutableLiveData<Boolean>()
    var signPlusLd: LiveData<Boolean> = mutableSignPlusLd

    private val mutableSignMinusLd = MutableLiveData<Boolean>()
    var signMinusLd: LiveData<Boolean> = mutableSignMinusLd

    private val mutableSignMultiplyLd = MutableLiveData<Boolean>()
    var signMultiplyLd: LiveData<Boolean> = mutableSignMultiplyLd

    private val mutableSignDivisionLd = MutableLiveData<Boolean>()
    var signDivisionLd: LiveData<Boolean> = mutableSignDivisionLd

    init {
        application.app.appComponent.injectTo(this)
        setPlusSign()
        setMinusSign()
        setMultiplySign()
        setDivisionSign()
    }

    override fun onPlusSignClick(): Boolean {

        if(prefHelper.isLastSign() && prefHelper.isPlusSignOn)
            return false
        prefHelper.isPlusSignOn = !prefHelper.isPlusSignOn
        setPlusSign()
        return true
    }

    override fun onMinusSignClick(): Boolean {

        if(prefHelper.isLastSign() && prefHelper.isMinusSignOn)
            return false
        prefHelper.isMinusSignOn = !prefHelper.isMinusSignOn
        setMinusSign()
        return true
    }

    override fun onMultiplySignClick(): Boolean {

        if(prefHelper.isLastSign() && prefHelper.isMultiplySignOn)
            return false
        prefHelper.isMultiplySignOn = !prefHelper.isMultiplySignOn
        setMultiplySign()
        return true
    }

    override fun onDivisionSignClick(): Boolean {

        if(prefHelper.isLastSign() && prefHelper.isDivisionSignOn)
            return false
        prefHelper.isDivisionSignOn = !prefHelper.isDivisionSignOn
        setDivisionSign()
        return true
    }

    private fun setPlusSign() {
        mutableSignPlusLd.value = prefHelper.isPlusSignOn
    }

    private fun setMinusSign() {
        mutableSignMinusLd.value = prefHelper.isMinusSignOn
    }

    private fun setMultiplySign() {
        mutableSignMultiplyLd.value = prefHelper.isMultiplySignOn
    }

    private fun setDivisionSign() {
        mutableSignDivisionLd.value = prefHelper.isDivisionSignOn
    }
    //endregion

    fun getTheme() = prefHelper.colorTheme

    fun setTheme(theme: Int) {
        prefHelper.colorTheme = theme
    }
    fun isSoundOn() = prefHelper.isSoundOn

    fun setSound(isOn: Boolean) {
        prefHelper.isSoundOn = isOn
    }
    fun getHighScore() = prefHelper.highScore

    fun setHighScore(highScore: Int) {
        prefHelper.highScore = highScore
    }
}