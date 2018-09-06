package com.storiqa.market.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.facebook.AccessToken
import com.storiqa.market.domain.AuthInteractor
import com.storiqa.market.domain.ApiClientInteractor
import com.storiqa.market.presentation.base.BasePresenter
import com.storiqa.market.util.log
import javax.inject.Inject

@InjectViewState
class NavigationPresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val clientInteractor: ApiClientInteractor
) : BasePresenter<NavigationView>() {

    fun checkLocalAuth() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedInFB = accessToken != null && !accessToken.isExpired
        log("isLoggedIn -> $isLoggedInFB")

        // todo handle expired token

        if (authInteractor.isLocalAuth()) {
            viewState.hideLoginView()
        } else if (isLoggedInFB) {
            // change fb token to our
            authInteractor.loginWithFB(accessToken.token)
                    .subscribe(
                            { data ->
                                log("change token result token -> ${data.data()?.jwtByProvider?.token()}")
                            },
                            { error ->
                                log("change token result token -> $error}")
                            }
                    )
                    .connect()

        }

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
                .connect()
    }

    fun onLogout() {
        authInteractor.logout()
        viewState.showLoginView()
    }

}