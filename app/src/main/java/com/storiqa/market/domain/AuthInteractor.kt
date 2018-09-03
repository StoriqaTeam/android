package com.storiqa.market.domain

import com.apollographql.apollo.api.Response
import com.storiqa.market.model.ServerResponse
import com.storiqa.market.model.repository.AuthRepository
import com.storiqa.market.util.log
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

     fun loginAlt(login: String, pass: String): Single<ServerResponse<Login_Mutation.Data>> =
            authRepository.loginAlt(login, pass)
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