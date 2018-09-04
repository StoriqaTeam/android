package com.storiqa.market.presentation.main

import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.arellomobile.mvp.InjectViewState
import com.storiqa.market.R
import com.storiqa.market.domain.AuthInteractor
import com.storiqa.market.domain.ApiClientInteractor
import com.storiqa.market.model.reponse.ErrorMessage
import com.storiqa.market.model.reponse.MarketException
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
                            val stt = data.data()?.currencies()?.joinToString(", ") { it.name }
                            log("currencies -> $stt")
                        },
                        { error -> log("currencies error -> $error") }
                )

    }

    fun onGetLangsClicked() {
        clientInteractor.getLangsWithSideEffect()
                .subscribe(
                        { result ->
                            if (result.data != null) {
                                val successData = result.data.successData?.languages()?.joinToString(", ") { it.isoCode() }
                                viewState.showLangsText(successData ?: "")
                            } else {
                                log("errorText-> ${result.errorMessage}")
                                viewState.showLangsText("${result.errorMessage}")
                            }
                        },
                        { error -> log("some exotic error -> $error") }
                )
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
                            if (response.finalSuccess) {
                                response.successData?.jwtByEmail?.token()?.let {
                                    viewState.hideLoginView()
                                }
                            } else {
                                val msg = "exc: \n " +
                                        "code -> ${response.errorDetails.errorCode} " +
                                        "payload -> ${response.errorDetails.payload}"
                                log(msg)
                            }
                            log(" token -> ${response.successData?.jwtByEmail?.token()}/end")
                        },
                        { error ->
                            when (error) {
                                is MarketException ->
                                    error.errorDetails.payload?.let {
                                        viewState.showErrorDetails(error.errorDetails.payload)
                                    }
                                is ApolloNetworkException ->
                                    viewState.showErrorDetails(R.string.no_connection_msg)
                                is ApolloException ->
                                    viewState.showErrorDetails("just apollo exception")
                                else ->
                                    viewState.showErrorDetails(ErrorMessage.DEFAULT_SERVER_ERROR.name)
                            }
                        }
                )
    }

    fun onLogout() {
        authInteractor.logout()
        viewState.showLoginView()
    }

}