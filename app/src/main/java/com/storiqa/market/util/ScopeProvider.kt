package com.storiqa.market.util

abstract class ScopeProvider<Component> {

    protected var component: Component? by ScopeDelegate()

    fun get() = component ?: open()

    fun close() {
        log("close fun of component -> ${component.toString()}")
        component = null
    }

    abstract fun open(): Component

    protected fun set(newComponent: Component): Component {
        component = newComponent
        log("set fun of component -> ${newComponent.toString()}")

        return component!!
    }
}
