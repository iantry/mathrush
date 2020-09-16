package com.iantry.mathrush.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.iantry.mathrush.R

/**
 * Created by Andrey on 07.01.2018.
 */
class SoundEffects internal constructor(context: Context) {

        private val soundPool: SoundPool

        private var soundWin = 0
        private var soundNext = 0
        private var soundGameOver = 0
        private var playSound: Boolean

        init {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(3)
                .build()

            playSound = true

            try {
                soundWin = soundPool.load(context, R.raw.win, 1)
                soundNext = soundPool.load(context, R.raw.next, 1)
                soundGameOver = soundPool.load(context, R.raw.gameover, 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun setPlaySound(playSound: Boolean) {
            this.playSound = playSound
        }

        fun playWin() {
            if (playSound) soundPool.play(soundWin, 0.35f, 0.35f, 0, 0, 1f)
        }

        fun playNext() {
            if (playSound) soundPool.play(soundNext, 0.4f, 0.4f, 0, 0, 1f)
        }

        fun playGameOver() {
            if (playSound) soundPool.play(soundGameOver, 0.4f, 0.4f, 0, 0, 1f)
        }
}