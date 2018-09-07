package com.storiqa.market.domain

import com.storiqa.market.model.repository.AuthRepository
import io.reactivex.Single

class AuthInteractor constructor(
        private val authRepository: AuthRepository
) {

    fun isLocalAuth() = authRepository.isLogged()

     fun login(login: String, pass: String): Single<Login_Mutation.Data> =
            authRepository.login(login, pass)
                    .doOnSuccess{ it ->
                        //this block is just side-effect to handle success response
                        authRepository.saveToken( "Bearer ${it.jwtByEmail.token()}" )
                    }


    fun logout() {
        authRepository.logout()
    }
}