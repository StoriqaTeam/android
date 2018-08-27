package com.storiqa.market.presentation

import com.arellomobile.mvp.MvpView

interface MainView : MvpView {
    fun showCurrenciesText(text: String)
}