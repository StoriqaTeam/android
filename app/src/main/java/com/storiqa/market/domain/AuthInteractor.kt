package com.storiqa.market.domain

import com.storiqa.market.model.reponse.AuthResult
import com.storiqa.market.model.repository.AuthRepository
import io.reactivex.Single

class AuthInteractor constructor(
        private val authRepository: AuthRepository
) {

    fun isLocalAuth() = authRepository.isLogged()

     fun login(login: String, pass: String): Single<AuthResult> =
            authRepository.login(login, pass)
                    .doOnSuccess{ it ->
                        //this block is just side-effect to handle success response

                        if (it.marketResp != null) {
                            //all other nullables ware checked in repository
                            authRepository.saveToken(
                                    "Bearer ${it.marketResp.jwtByEmail.token()}"
                            )
                        }
                    }


    fun logout() {
        authRepository.logout()
    }
}