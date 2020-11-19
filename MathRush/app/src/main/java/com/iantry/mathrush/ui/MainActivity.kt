package com.iantry.mathrush.ui

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.iantry.mathrush.*
import com.iantry.mathrush.repository.PrefHelper.Companion.THEME_DAY
import com.iantry.mathrush.repository.PrefHelper.Companion.THEME_NIGHT

import com.iantry.mathrush.viewmodel.EquationViewModel
import com.iantry.mathrush.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var soundEffects: SoundEffects
    @Inject
    lateinit var equationViewModel: EquationViewModel
    @Inject
    lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        application.app.initMainActivityComponent(this).injectTo(this)
        setSupportActionBar(toolbar)
        setTitle(R.string.app_name)

        measureSoundEffects()
        setButtonClickListeners()
        subscribeToData()
        subscribeToEvents()
        initTheme()
    }


    private fun measureSoundEffects() {
        soundEffects.setPlaySound(settingViewModel.isSoundOn())
    }

    private fun setButtonClickListeners() {
        correctBtn.setOnClickListener { equationViewModel.onCorrectBtnClick() }

        incorrectBtn.setOnClickListener { equationViewModel.onIncorrectBtnClick() }

        restartButton.setOnClickListener {
            soundEffects.playNext()
            Animator.startRestartButtonAnimation(restartLayout, Animator.STATE_HIDE)
            Animator.returnAnswerButtons(correctAnimateView, incorrectAnimateView)
            tvScorePoints.text = "0"
            hideCorrectAnswer()
            buttonsActive()

            equationViewModel.onRestartBtnClick()
        }
    }

    private fun subscribeToData() {
        equationViewModel.equationLd.observe(this) { setEquation(it) }

        equationViewModel.scoreLd.observe(this) { setScoreText(it) }

        settingViewModel.signPlusLd.observe(this) { setPlusSign(it) }

        settingViewModel.signMinusLd.observe(this) { setMinusSign(it) }

        settingViewModel.signMultiplyLd.observe(this, { setMultiplySign(it) })

        settingViewModel.signDivisionLd.observe(this, { setDivisionSign(it) })
    }

    private fun subscribeToEvents(){
        equationViewModel.isCorrectAnswerLd.observe(this, {
            it.getContentIfNotHandled()?.let{answerEvent -> // Only proceed if the event has never been handled
                if(answerEvent.first) correct(answerEvent.second)
                else incorrect()
            }
        })
    }

    private fun generateNewEquations()= equationViewModel.generateNewEquations()

    private val incorrect = {

        soundEffects.playGameOver()
        showCorrectAnswer(equationViewModel.equationLd.value)
        buttonsInactive()

        Animator.stopAnswerButtonsAnimation()
        Animator.fillProgressAnswerButtons(correctAnimateView, incorrectAnimateView)
        Animator.startRestartButtonAnimation(restartLayout, Animator.STATE_SHOW)
    }

    private fun correct(comboMultiplier: Int) {

        soundEffects.playWin()
        buttonsInactive()

        if(comboMultiplier > 1)
            Animator.startComboAnimation(tvCombo, comboMultiplier)

        val equationHalfAnimListener = object: CompletionBlock {
            override fun onComplete() {
                equationViewModel.requestNewEquation()
                Animator.returnAnswerButtons(correctAnimateView, incorrectAnimateView)
            }
        }

        val equationAnimCompleteListener = object : CompletionBlock {
            override fun onComplete() {

                soundEffects.playNext()
                buttonsActive()

                Animator.startAnswerButtonsAnimation(correctAnimateView, incorrectAnimateView,
                    //halfCompletionBlock
                    object : CompletionBlock {
                        override fun onComplete() {
                            // сделать через функцию
                            equationViewModel.isUntilEquationHalfTime = false
                        }
                    },
                    //onFullTimeOver
                    object : CompletionBlock {
                        override fun onComplete() = equationViewModel.onTimeOver()
                    })
            }
        }
        Animator.stopAnswerButtonsAnimation()
        Animator.startEquationAnimation(
            linerLayoutEquation,
            equationHalfAnimListener,
            equationAnimCompleteListener
        )
        // сделать через функцию
        equationViewModel.isUntilEquationHalfTime = true
    }

    val buttonsActive = {
        correctBtn.isClickable = true
        incorrectBtn.isClickable = true
    }
    val buttonsInactive = {
        correctBtn.isClickable = false
        incorrectBtn.isClickable = false
    }

    private fun showCorrectAnswer(equation: Equation?) {

        if(equation?.isCorrect != false)
            result.setTextColor(ContextCompat.getColor(this, R.color.colorButtonCorrect))
        else {
            val lp = resultFrame.layoutParams as LinearLayout.LayoutParams
            lp.leftMargin = 0
            resultCrossOut.visibility = View.VISIBLE
            correctResult.text = equation.correctAnswer.toString()
            correctResult.visibility = View.VISIBLE
        }
    }

    private fun hideCorrectAnswer() {
        if(correctResult.visibility == View.GONE)
            result.setTextColor(ContextCompat.getColor(this, R.color.colorText))
        else {
            val lp = resultFrame.layoutParams as LinearLayout.LayoutParams
            lp.leftMargin = dpToPx(7).toInt()
            resultCrossOut.visibility = View.GONE
            correctResult.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {

        var isSignChanged = false

        when(v?.id) {
            plusIconFrame.id -> isSignChanged = settingViewModel.onPlusSignClick()
            minusIconFrame.id -> isSignChanged = settingViewModel.onMinusSignClick()
            multiplyIconFrame.id -> isSignChanged = settingViewModel.onMultiplySignClick()
            divisionIconFrame.id -> isSignChanged = settingViewModel.onDivisionSignClick()
        }
        if(isSignChanged) {
            generateNewEquations()
            setScoreText()
        }
    }

    val setPlusSign = {isOn: Boolean ->
        if(isOn) { plusIcon.setImageResource(R.drawable.ic_plus); tvPlusIcon?.visibility = View.VISIBLE }
        else { plusIcon.setImageResource(R.drawable.ic_plus_off); tvPlusIcon?.visibility = View.GONE }
    }

    val setMinusSign = {isOn: Boolean ->
        if(isOn) { minusIcon.setImageResource(R.drawable.ic_minus); tvMinusIcon ?.visibility = View .VISIBLE }
        else { minusIcon.setImageResource(R.drawable.ic_minus_off); tvMinusIcon?.visibility = View.GONE }
    }

    val setMultiplySign = {isOn: Boolean ->
        if(isOn) { multiplyIcon.setImageResource(R.drawable.ic_multiply); tvMultiplyIcon?.visibility = View.VISIBLE }
        else { multiplyIcon.setImageResource(R.drawable.ic_multiply_off); tvMultiplyIcon?.visibility = View.GONE }
    }

    val setDivisionSign = {isOn: Boolean ->
        if(isOn) { divisionIcon.setImageResource(R.drawable.ic_division); tvDivisionIcon?.visibility = View.VISIBLE }
        else { divisionIcon.setImageResource(R.drawable.ic_division_off); tvDivisionIcon?.visibility = View.GONE }
    }

    private fun initTheme() {
        when (settingViewModel.getTheme()) {
            THEME_DAY -> setUiTheme(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_NIGHT -> setUiTheme(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun setUiTheme(themeMode: Int) = AppCompatDelegate.setDefaultNightMode(themeMode)

    private fun saveTheme(theme: Int) = settingViewModel.setTheme(theme)

    private fun setMenuIcon(menuItem: MenuItem, iconRes: Int) {
        menuItem.icon = ContextCompat.getDrawable(this, iconRes)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val colorThemeItem = menu.findItem(R.id.colorTheme)
        val soundItem = menu.findItem(R.id.sound)

        when (settingViewModel.isSoundOn()) {
            true -> setMenuIcon(soundItem, R.drawable.ic_sound_on)
            false -> setMenuIcon(soundItem, R.drawable.ic_sound_off)
        }

        when (settingViewModel.getTheme()) {
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
                if(settingViewModel.isSoundOn()) {
                    settingViewModel.setSound(false)
                    setMenuIcon(item, R.drawable.ic_sound_off)
                    soundEffects.setPlaySound(false)
                }
                else {
                    settingViewModel.setSound(true)
                    setMenuIcon(item, R.drawable.ic_sound_on)
                    soundEffects.setPlaySound(true)
                }
            R.id.feedback -> {
                val playIntent = Intent(Intent.ACTION_VIEW)
                playIntent.data = Uri.parse("market://details?id=com.iantry.mathrush")
                startActivity(playIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setScoreText(currentScore: Int = 0) {
        if(currentScore != 0) {
            if(currentScore > settingViewModel.getHighScore()) {
                settingViewModel.setHighScore(currentScore)
                tvHighScorePoints.text = currentScore.toString()
                tvScorePoints.text = currentScore.toString()
            }
            else tvScorePoints.text = currentScore.toString()
        }
        else {
            tvHighScorePoints.text = settingViewModel.getHighScore().toString()
            tvScorePoints.text = "0"
        }
    }

    private fun setEquation(equation: Equation) {
        firstNumber.text = equation.value1.toString()
        sign.text = equation.sign
        secondNumber.text = equation.value2.toString()
        result.text = equation.answer.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundEffects.setPlaySound(false)
    }
}