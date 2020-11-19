package com.iantry.mathrush

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


fun SharedPreferences.intDelegate(defaultValue: Int = 1, key: (KProperty<*>)->String = {it.name}): ReadWriteProperty<Any, Int> {

    return object: ReadWriteProperty<Any, Int> {

        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getInt(key(property), defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
            edit().putInt(key(property), value).apply()
    }
}

fun SharedPreferences.booleanDelegate(defaultValue: Boolean = false, key: (KProperty<*>)->String = KProperty<*>::name): ReadWriteProperty<Any, Boolean> {

    return object: ReadWriteProperty<Any, Boolean> {

        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getBoolean(key(property), defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
          edit().putBoolean(key(property), value).apply()
    }
}

class TrimDelegate : ReadWriteProperty<Any?, String> {

    private var trimmedValue: String = ""

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return trimmedValue
    }
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        trimmedValue = value.trim()
    }
}

//class Example {
//    private val delegate = TrimDelegate()
//    var param: String
//        get() = delegate.getValue(this, ::param)
//        set(value) {
//            delegate.setValue(this, ::param, value)
//        }
//}


