package com.iantry.mathrush

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import java.util.*


class Animator {

    companion object {

        const val STATE_SHOW = 1
        const val STATE_HIDE = 2

        private val displayWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
        private val displayHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()

        lateinit var timer: Timer
        private lateinit var answerButtonAnimSet: AnimatorSet
        private var comboViewStartPos = -1f

        @JvmStatic
        fun startRestartButtonAnimation(view: View, state: Int) {

            val viewHeight = view.height.toFloat()
            val animationY =
                if (state == 1) ObjectAnimator.ofFloat(view, "translationY", displayHeight + dpToPx(5), 0f)
                else ObjectAnimator.ofFloat(view, "translationY", 0f, viewHeight)

            AnimatorSet().apply {
                duration = 350
                play(animationY)
                start()
            }
        }


        fun startAnswerButtonsAnimation(b1: View, b2: View,
                                        halfCompletionBlock: CompletionBlock,
                                        completionBlock: CompletionBlock) {

            val viewWidth = b1.height.toFloat()
            val animationDuration = 3000L
            val b1Anim = ObjectAnimator.ofFloat(b1, "translationX", viewWidth, 0f)
            val b2Anim = ObjectAnimator.ofFloat(b2, "translationX", viewWidth, 0f)

            b1Anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    completionBlock.onComplete()
                }
                override fun onAnimationCancel(animation: Animator?) {
                    animation?.removeAllListeners()
                    super.onAnimationCancel(animation)
                }
            })

            answerButtonAnimSet = AnimatorSet().apply {
                duration = animationDuration
                interpolator = LinearInterpolator()
                play(b1Anim).with(b2Anim)
                start()
            }

            startTimer(animationDuration/2, halfCompletionBlock)
        }

        private fun startTimer(timerDelay: Long, halfCompletionBlock: CompletionBlock) {
            timer = Timer()
            timer.schedule(object: TimerTask() {
                override fun run() {
                    timer.cancel()
                    halfCompletionBlock.onComplete()
                }
            }, timerDelay)
        }

        fun stopAnswerButtonsAnimation() {

            if(::answerButtonAnimSet.isInitialized)
                if (answerButtonAnimSet.isRunning) {
                    timer.cancel()
                    answerButtonAnimSet.cancel()
                }
        }

        fun fillProgressAnswerButtons(b1: View, b2: View) {
            b1.translationX = 0f
            b2.translationX = 0f
        }

        fun returnAnswerButtons(b1: View, b2: View) {
            b1.translationX = b1.width.toFloat()
            b2.translationX = b2.width.toFloat()
        }


        fun startEquationAnimation(view: View, halfCompletionBlock: CompletionBlock?, completionBlock: CompletionBlock?) {

            val translationX = view.translationX
            val animationX = ObjectAnimator.ofFloat(view, "translationX", translationX, -displayWidth)
            val animationBackX = ObjectAnimator.ofFloat(view, "translationX", displayWidth, translationX)

            animationX.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    halfCompletionBlock?.onComplete()
                }
            })
            animationBackX.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    completionBlock?.onComplete()
                }})

            AnimatorSet().apply {
                duration = 250
                play(animationX).before(animationBackX)
                start()
            }
        }

        fun startComboAnimation(view: View, comboMultiplier: Int) {

            val textView = view as TextView
            textView.text = textView.text.toString().replace(Regex("[0-9]{1,4}"), comboMultiplier.toString())

            if(comboViewStartPos == -1f) comboViewStartPos = view.translationY

            val animY = ObjectAnimator.ofFloat(view, "translationY", comboViewStartPos, comboViewStartPos - dpToPx(55))
            val animAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)

            AnimatorSet().apply {
                duration = 1270
                play(animY).with(animAlpha)
                start()
            }
        }
    }
}