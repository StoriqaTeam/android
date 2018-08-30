package com.storiqa.market.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.storiqa.market.domain.AuthInteractor
import com.storiqa.market.domain.ClientInteractor
import com.storiqa.market.presentation.base.BasePresenter
import com.storiqa.market.util.log
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val clientInteractor: ClientInteractor
) : BasePresenter<MainView>() {

    fun checkLocalAuth() {
        if (authInteractor.isLocalAuth()) {
            viewState.hideLoginView()
        }

        clientInteractor.getCurrencies()
                .subscribe(
                        { data ->
                            val stt = data.data()?.currencies()?.joinToString(", ") { it.name }
                            log("currencies -> $stt")
                        },
                        { error -> log("currencies error -> $error") }
                )
    }

    fun onGetLangsClicked() {
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
    }

    fun onMeInfoRequested() {

        clientInteractor.getMeInfo()
                .subscribe(
                        { data ->
                            val stt = data.data()?.me()?.id()?: "empty id"
                            log("my id -> $stt")
                            viewState.showMeInfo(stt)
                        },
                        { error ->
                            log("Me_Query, error -> " + error.toString())
                        }
                )
                .connect()

    }

    fun onLogin(login: String, pass: String) {
        authInteractor.login(login, pass)
                .subscribe(
                        { response ->
                            log(" ae! resp -> $response")
                            response.data()?.jwtByEmail?.token()?.let {
                                viewState.hideLoginView()
                            }
                            log(" token -> ${response.data()?.jwtByEmail?.token()}/end")
                        },
                        { error ->
                            log("error -> $error")
                        }
                )
    }

    fun onLogout() {
        authInteractor.logout()
        viewState.showLoginView()
    }

}