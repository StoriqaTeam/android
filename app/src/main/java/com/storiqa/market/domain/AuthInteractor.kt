package com.storiqa.market.domain

import com.storiqa.market.model.repository.AuthRepository
import javax.inject.Inject

class AuthInteractor @Inject constructor(
        private val authRepository: AuthRepository
) {

    fun isLocalAuth() = authRepository.isLogged()

    fun login(login: String, pass: String) =
            authRepository.login(login, pass)
                    .doOnSuccess{ it ->
                        it.data()?.jwtByEmail?.token()?.let {
                            authRepository.saveToken( "Bearer $it" )
                        }
                    }

    fun logout() {
        authRepository.logout()
    }
}