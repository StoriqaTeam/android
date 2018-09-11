package com.storiqa.market.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.facebook.AccessToken
import com.storiqa.market.domain.AuthInteractor
import com.storiqa.market.domain.ApiClientInteractor
import com.storiqa.market.model.reponse.AuthException
import com.storiqa.market.model.reponse.MarketServException
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
                                log("change token result token -> ${data.jwtByProvider.token()}")
                                viewState.hideLoginView()
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
                        { result ->
                            val langs = result.languages().joinToString(", ") { it.isoCode() }
                            viewState.showLangsText(langs)
                        },
                        { error ->
                            when (error) {
                                is MarketServException -> {
                                    // put error instead langs text
                                    viewState.showLangsText(error.errorDetails.payload)
                                }
                                else -> {
                                    // show dialog with error
                                    viewState.showErrorDetails(error.localizedMessage)
                                }
                            }
                        }
                )
                .connect()
    }

    fun onMeInfoRequested() {

        clientInteractor.getMeInfo()
                .subscribe(
                        { data ->
                            val stt = data.me()!!.id()
                            log("my id -> $stt")
                            viewState.showMeInfo(stt)
                        },
                        { error ->
                            when (error) {
                                is MarketServException ->
                                    viewState.showErrorDetails(error.errorDetails.payload)
                                else -> viewState.showErrorDetails(error.localizedMessage)
                            }
                        }
                )
                .connect()
    }

    fun onLogin(login: String, pass: String) {
        clearErrorMessages()
        authInteractor.login(login, pass)
                .subscribe(
                        { response ->
                            viewState.hideLoginView()
                        },
                        { error ->
                            when (error) {
                                is AuthException -> {
                                    val authError = error.authError
                                    viewState.indicateEmailError(authError.emailProblem)
                                    viewState.indicatePassError(authError.passProblem)
                                }
                                is MarketServException ->
                                    viewState.showErrorDetails(error.errorDetails.payload)
                                else -> viewState.showErrorDetails(error.localizedMessage)
                            }
                        }
                )
                .connect()
    }

    private fun clearErrorMessages() {
        viewState.indicateEmailError(null)
        viewState.indicatePassError(null)
    }

    fun onLogout() {
        authInteractor.logout()
        viewState.showLoginView()
    }

}