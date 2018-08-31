package com.storiqa.market.presentation.main

import com.arellomobile.mvp.MvpView

interface NavigationView : MvpView {
    fun showLangsText(text: String)
    fun showMeInfo(text: String)
    fun hideLoginView()
    fun showLoginView()
}