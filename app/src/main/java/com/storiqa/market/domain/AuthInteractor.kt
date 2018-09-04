package com.storiqa.market.domain

import com.storiqa.market.model.reponse.MarketServerResponse
import com.storiqa.market.model.repository.AuthRepository
import com.storiqa.market.util.log
import io.reactivex.Single

class AuthInteractor constructor(
        private val authRepository: AuthRepository
) {

    fun isLocalAuth() = authRepository.isLogged()

     fun login(login: String, pass: String): Single<MarketServerResponse<Login_Mutation.Data>> =
            authRepository.login(login, pass)
                    .doOnSuccess{ it ->
                        log("auth repository, onSuccess errorDetails.payload-> ${it.errorDetails.payload}")
                        if (it.finalSuccess) {
                            it.successData?.jwtByEmail?.token()?.let {
                                authRepository.saveToken( "Bearer $it" )
                            }
                        }
                    }


    fun logout() {
        authRepository.logout()
    }
}