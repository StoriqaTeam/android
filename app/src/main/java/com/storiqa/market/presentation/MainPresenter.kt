package com.storiqa.market.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    fun onReadyToShow() {
        viewState.showCurrenciesText("sdfsdf")
    }

    fun loadLangs() {

    }

}