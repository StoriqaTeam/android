package com.storiqa.market.domain

import com.apollographql.apollo.api.Response
import com.storiqa.market.model.repository.AuthRepository
import io.reactivex.Single

class AuthInteractor constructor(
        private val authRepository: AuthRepository
) {

    fun isLocalAuth() = authRepository.isLogged()

    fun login(login: String, pass: String): Single<Response<Login_Mutation.Data>> =
            authRepository.login(login, pass)
                    .doOnSuccess{ it ->
                        it.data()?.jwtByEmail?.token()?.let {
                            authRepository.saveToken( "Bearer $it" )
                        }
                    }

    fun loginWithFB(providerToken: String): Single<Response<LoginProviderMutation.Data>> =
            authRepository.loginWithFB(providerToken)
                    .doOnSuccess { it ->
                        it.data()?.jwtByProvider?.token()?.let {
                            authRepository.saveToken( "Bearer $it" )
                        }
                    }

    fun logout() {
        authRepository.logout()
    }
}