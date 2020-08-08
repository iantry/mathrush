package com.iantry.mathrush

import android.content.Context
import android.content.res.Resources
import android.widget.Toast

fun dpToPx(dp: Int = 1) = (dp * Resources.getSystem().displayMetrics.density)

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, duration).show()
