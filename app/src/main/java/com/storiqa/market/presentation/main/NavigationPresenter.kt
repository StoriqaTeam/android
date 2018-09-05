package com.storiqa.market.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.storiqa.market.domain.AuthInteractor
import com.storiqa.market.domain.ApiClientInteractor
import com.storiqa.market.model.reponse.ResultVariants
import com.storiqa.market.presentation.base.BasePresenter
import com.storiqa.market.util.log
import javax.inject.Inject

@InjectViewState
class NavigationPresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val clientInteractor: ApiClientInteractor
) : BasePresenter<NavigationView>() {

    fun checkLocalAuth() {
        if (authInteractor.isLocalAuth()) {
            viewState.hideLoginView()
        }

        clientInteractor.getCurrencies()
                .subscribe(
                        { data ->
                            val stt = data.successData?.currencies()?.joinToString(", ") { it.name }
                            log("currencies -> $stt")
                        },
                        { error -> log("currencies error -> $error") }
                )
    }

    fun onGetLangsClicked() {
        clientInteractor.getLanguages()
                .subscribe(
                        { result ->
                            if (result.successData != null) {
                                val langs = result.successData.languages().joinToString(", ") { it.isoCode() }
                                viewState.showLangsText(langs)
                            } else {
                                log("errorText-> ${result.errorDetails.payload}")
                                viewState.showLangsText(result.errorDetails.payload)
                            }
                        },
                        { error -> log("some exotic error -> $error") }
                )
    }

    fun onMeInfoRequested() {

        clientInteractor.getMeInfo()
                .subscribe(
                        { data ->
                            val stt = data.successData?.me()?.id()?: "empty id"
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
        clearErrorMessages()
        authInteractor.login(login, pass)
                .subscribe(
                        { response ->
                            when (response.resVariant) {
                                ResultVariants.SUCCESS -> {
                                    viewState.hideLoginView()
                                }
                                ResultVariants.COMMON_ERROR -> {
                                    viewState.showErrorDetails(response.commonError!!)
                                }
                                ResultVariants.DETAILED_ERRORS -> {
                                    val msg = "email -> ${response.emailError} " +
                                            "pass -> ${response.passError}"
                                    log(msg)
                                    viewState.indicateEmailError(response.emailError)
                                    viewState.indicatePassError(response.passError)
                                }
                            }
                        },
                        { /*errors are handled in repo and wrapped by AuthResult class*/ }
                )
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