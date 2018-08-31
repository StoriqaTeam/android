package com.storiqa.market.util

import kotlin.reflect.KProperty

class ScopeDelegate<Component> {

    private var component: Component? = null
    private var counter: Int = 0

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Component? = component

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Component?) =
        value?.let {
            counter++
            component = value
        } ?: run {
            component = if (--counter == 0) null else return
        }
}