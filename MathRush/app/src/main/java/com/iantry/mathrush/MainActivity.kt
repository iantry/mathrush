package com.iantry.mathrush

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.iantry.mathrush.SharedPreferenceHelper.Companion.THEME_DAY
import com.iantry.mathrush.SharedPreferenceHelper.Companion.THEME_NIGHT
import com.iantry.mathrush.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binder: ActivityMainBinding
    private lateinit var prefHelper : SharedPreferenceHelper
    private lateinit var soundEffects: SoundEffects


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)
        prefHelper = SharedPreferenceHelper(this)
        soundEffects = SoundEffects(this)
        soundEffects.setPlaySound(prefHelper.isSoundOn)
        initTheme()
        setSupportActionBar(toolbar)
        setTitle(R.string.app_name)
        setSigns()
        showEquation()
    }

    val buttonsActive = {
        equationCorrect.isClickable = true
        equationIncorrect.isClickable = true
    }
    val buttonsInactive = {
        equationCorrect.isClickable = false
        equationIncorrect.isClickable = false
    }

    private fun showEquation() {

        //getHighScore(prefHelper.gameMode)
        val scoreCounter = ScoresCounter()

        val equationsGetter = EquationsGetter(this)
        val newEquation = { equationsGetter.retrieveEquation() }
        var equationPair = newEquation()

        var isUntilHalfTime = true
        var comboMultiplier = 1


        setEquation(equationPair.first)
        tvHighScorePoints.text = prefHelper.highScore.toString()

        fun incorrect() {
            soundEffects.playGameOver()
            scoreCounter.resetScores()
            buttonsInactive()
            Animator.stopAnswerButtonsAnimation()
            Animator.fillProgressAnswerButtons(correctAnimateView, incorrectAnimateView)
            Animator.startRestartButtonAnimation(restartLayout, Animator.STATE_SHOW)
        }

        val correct = {
            soundEffects.playWin()
            buttonsInactive()

            if(isUntilHalfTime && scoreCounter.totalScores > 0) {
                comboMultiplier++
                Animator.startComboAnimation(binder.combo, comboMultiplier)
            }

            val currentScore = scoreCounter.calculateScores(comboMultiplier)
            tvScorePoints.text = currentScore.toString()

            if(currentScore > prefHelper.highScore) {
                prefHelper.highScore = currentScore
                tvHighScorePoints.text = currentScore.toString()
            }

            val equationHalfAnimListener = object: CompletionBlock {

                override fun onComplete() {
                    equationPair = newEquation()
                    setEquation(equationPair.first)
                    Animator.returnAnswerButtons(binder.correctAnimateView, binder.incorrectAnimateView)
                }
            }

            val equationAnimCompleteListener = object : CompletionBlock {
                override fun onComplete() {

                    soundEffects.playNext()
                    buttonsActive()

                    Animator.startAnswerButtonsAnimation(binder.correctAnimateView, binder.incorrectAnimateView,
                        //halfCompletionBlock
                        object: CompletionBlock {
                            override fun onComplete() {
                                isUntilHalfTime = false
                                comboMultiplier = 1
                            }
                        },
                        object: CompletionBlock {
                            override fun onComplete() = incorrect()
                        })
                }
            }
            Animator.stopAnswerButtonsAnimation()
            Animator.startEquationAnimation(binder.equation, equationHalfAnimListener, equationAnimCompleteListener)
            isUntilHalfTime = true
        }

        binder.equationCorrect.setOnClickListener {
            if (equationPair.second) correct()
            else incorrect()
        }

        binder.equationIncorrect.setOnClickListener {
            if (equationPair.second) incorrect()
            else correct()
        }

        binder.restartButton.setOnClickListener {

            soundEffects.playNext()
            Animator.startRestartButtonAnimation(restartLayout, Animator.STATE_HIDE)
            Animator.returnAnswerButtons(binder.correctAnimateView, binder.incorrectAnimateView)
            tvScorePoints.text = "0"

            equationPair = newEquation()
            setEquation(equationPair.first)
            buttonsActive()
            comboMultiplier = 1
        }
    }


    private fun initTheme() {
        when (prefHelper.colorTheme) {
            THEME_DAY -> setUiTheme(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_NIGHT -> setUiTheme(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun setUiTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    private fun saveTheme(theme: Int) {
        prefHelper.colorTheme = theme
    }

    private fun setMenuIcon(menuItem: MenuItem, iconRes: Int) {
        menuItem.icon = ContextCompat.getDrawable(this, iconRes)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val colorThemeItem = menu.findItem(R.id.colorTheme)
        val soundItem = menu.findItem(R.id.sound)

        when (prefHelper.isSoundOn) {
            true -> setMenuIcon(soundItem, R.drawable.ic_sound_on)
            false -> setMenuIcon(soundItem, R.drawable.ic_sound_off)
        }

        when (prefHelper.colorTheme) {
            THEME_DAY -> setMenuIcon(colorThemeItem, R.drawable.ic_sun)
            THEME_NIGHT -> setMenuIcon(colorThemeItem, R.drawable.ic_moon)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.colorTheme ->
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        setUiTheme(AppCompatDelegate.MODE_NIGHT_YES)
                        setMenuIcon(item, R.drawable.ic_moon)
                        saveTheme(THEME_NIGHT)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        setUiTheme(AppCompatDelegate.MODE_NIGHT_NO)
                        setMenuIcon(item, R.drawable.ic_sun)
                        saveTheme(THEME_DAY)
                    }
                }
            R.id.sound ->
                if(prefHelper.isSoundOn) {
                    prefHelper.isSoundOn = false
                    setMenuIcon(item, R.drawable.ic_sound_off)
                    soundEffects.setPlaySound(false)
                }
                else {
                    prefHelper.isSoundOn = true
                    setMenuIcon(item, R.drawable.ic_sound_on)
                    soundEffects.setPlaySound(true)
                }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {

        when(v?.id) {
            plusIconFrame.id -> {
                if (prefHelper.isPlusSignOn) {
                    if(isNotLastSign()) {
                        prefHelper.isPlusSignOn = false
                        prepareNewEquations(1)
                    }
                } else {
                    prefHelper.isPlusSignOn = true
                    prepareNewEquations( 1)
                }
            }
            minusIconFrame.id -> {
                if (prefHelper.isMinusSignOn) {
                    if(isNotLastSign())  {
                        prefHelper.isMinusSignOn = false
                        prepareNewEquations( 2)
                    }
                } else {
                    prefHelper.isMinusSignOn = true
                    prepareNewEquations( 2)
                }
            }
            multiplyIconFrame.id -> {
                if (prefHelper.isMultiplySignOn) {
                    if(isNotLastSign()) {
                        prefHelper.isMultiplySignOn = false
                        prepareNewEquations( 3)
                    }
                } else {
                    prefHelper.isMultiplySignOn = true
                    prepareNewEquations( 3)
                }
            }
            divisionIconFrame.id -> {
                if (prefHelper.isDivisionSignOn) {
                    if(isNotLastSign()) {
                        prefHelper.isDivisionSignOn = false
                        prepareNewEquations( 4)
                    }
                } else {
                    prefHelper.isDivisionSignOn = true
                    prepareNewEquations( 4)
                }
            }
        }
    }

    private fun prepareNewEquations(signType: Int = 0) {
        setScoreText()
        setSigns(signType)
        showEquation()
        buttonsInactive()
    }

    private fun setScoreText() {
        tvHighScorePoints.text = prefHelper.highScore.toString()
        tvScorePoints.text = "0"
    }

    private fun setSigns(signType: Int = 0) {

        val setPlusSign = {
            if(prefHelper.isPlusSignOn) { plusIcon.setImageResource(R.drawable.ic_plus); tvPlusIcon?.visibility = View.VISIBLE }
            else { plusIcon.setImageResource(R.drawable.ic_plus_off); tvPlusIcon?.visibility = View.GONE }
        }

        val setMinusSign = {
            if(prefHelper.isMinusSignOn) { minusIcon.setImageResource(R.drawable.ic_minus); tvMinusIcon ?.visibility = View .VISIBLE }
            else { minusIcon.setImageResource(R.drawable.ic_minus_off); tvMinusIcon?.visibility = View.GONE }
        }

        val setMultiplySign = {
            if(prefHelper.isMultiplySignOn) {
                multiplyIcon.setImageResource(R.drawable.ic_multiply); tvMultiplyIcon?.visibility = View.VISIBLE }
            else { multiplyIcon.setImageResource(R.drawable.ic_multiply_off); tvMultiplyIcon?.visibility = View.GONE }
        }

        val setDivisionSign = {
            if(prefHelper.isDivisionSignOn) {
                divisionIcon.setImageResource(R.drawable.ic_division) ;tvDivisionIcon?.visibility = View.VISIBLE }
            else { divisionIcon.setImageResource(R.drawable.ic_division_off); tvDivisionIcon?.visibility = View.GONE }
        }

        when(signType) {
            1 -> setPlusSign()
            2 -> setMinusSign()
            3 -> setMultiplySign()
            4 -> setDivisionSign()
            else -> {
                setPlusSign()
                setMinusSign()
                setMultiplySign()
                setDivisionSign()
            }
        }
    }

    private fun isNotLastSign(): Boolean {
        var counter = 0
        if(prefHelper.isPlusSignOn)
            counter++
        if(prefHelper.isMinusSignOn)
            counter++
        if(prefHelper.isMultiplySignOn)
            counter++
        if(prefHelper.isDivisionSignOn)
            counter++
        return counter > 1
    }

    private fun setEquation(equation: ArrayList<String>) {
        firstNumber.text = equation[0]
        sign.text = equation[1]
        secondNumber.text = equation[2]
        result.text = equation[3]
    }

    override fun onDestroy() {
        super.onDestroy()
        soundEffects.setPlaySound(false)
    }
}