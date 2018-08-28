package com.storiqa.market.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.storiqa.market.domain.ClientInteractor
import com.storiqa.market.presentation.base.BasePresenter
import com.storiqa.market.util.log
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val clientInteractor: ClientInteractor
) : BasePresenter<MainView>() {

    fun onReadyToShow() {

        clientInteractor.getLanguages()
                .subscribe(
                        { data ->
                            val stt = data.data()?.languages()?.joinToString(", ") { it.isoCode() }
                            log("languages -> $stt")
                            viewState.showLangsText(stt?: "empty")
                        },
                        { error ->
                            log("Languages_Query, error -> " + error.toString())
                        }
                )
                .connect()

        clientInteractor.getMeInfo()
                .subscribe(
                        { data ->
                            val stt = data.data()?.me()?.id()?: "empty id"
                            log("my id -> $stt")
                            viewState.showLangsText(stt)
                        },
                        { error ->
                            log("Me_Query, error -> " + error.toString())
                        }
                )
                .connect()

    }

}