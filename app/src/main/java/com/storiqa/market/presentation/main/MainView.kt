package com.storiqa.market.presentation.main

import com.arellomobile.mvp.MvpView

interface MainView : MvpView {
    fun showLangsText(text: String)
    fun showMeInfo(text: String)
    fun hideLoginView()
    fun showLoginView()
}