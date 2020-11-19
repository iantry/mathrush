package com.iantry.mathrush

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import kotlin.properties.ReadWriteProperty
import kotlin.random.Random
import kotlin.reflect.KProperty

fun dpToPx(dp: Int = 1) = (dp * Resources.getSystem().displayMetrics.density)

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, duration).show()

/**
 * return 0 or 1 with default arguments
 * in other return random between "from" and "to" included
 */
fun Any.rand(from: Int = 0, to: Int = 1): Int {
    val random = from + Random.nextInt(to + 1)
    return if(random > to) to
    else random
}

fun Any.positive(value: Int = 0) = if(value < 0) -1 * value else value

fun Any.log(message: String) {
    Log.d("Log", message)
}

val Application.app: App
    get() = this as App
