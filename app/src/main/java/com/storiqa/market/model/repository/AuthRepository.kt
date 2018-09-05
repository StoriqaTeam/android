package com.storiqa.market.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.storiqa.market.model.data.auth.AuthHolder
import com.storiqa.market.model.reponse.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import type.CreateJWTEmailInput

class AuthRepository constructor(
        private val authData: AuthHolder,
        private val client: ApolloClient
) {

    fun isLogged() = !authData.token.isNullOrEmpty()

    fun saveToken(token: String) {
        authData.token = token
    }

    fun logout() {
        authData.token = null
    }

    fun login(login: String, pass: String): Single<AuthResult> =
            (Rx2Apollo.from<Login_Mutation.Data>(
                    client.mutate(
                            Login_Mutation
                                    .builder()
                                    .input(
                                            CreateJWTEmailInput
                                                    .builder()
                                                    .clientMutationId("")
                                                    .email(login)
                                                    .password(pass)
                                                    .build())
                                    .build()
                    )
            ))
                    .map { it ->
                        val resp = MarketServerResponse(it)
                        if (resp.finalSuccess && !resp.successData?.jwtByEmail?.token().isNullOrEmpty()) {
                            AuthResult(
                                    resVariant = ResultVariants.SUCCESS,
                                    marketResp = resp.successData
                            )
                        } else {
                            val authPayload = AuthErrorPayload(resp.errorDetails.payload)
                            AuthResult(
                                    resVariant = ResultVariants.DETAILED_ERRORS,
                                    emailError = authPayload.emailProblem,
                                    passError = authPayload.passProblem
                            )
                        }
                    }
                    .onErrorReturn{ throwable ->
                        when (throwable) {
                            is ApolloNetworkException ->
                                AuthResult(
                                        resVariant = ResultVariants.COMMON_ERROR,
                                        commonError = "no connection to server"
                                )
                            else ->
                                AuthResult(
                                        resVariant = ResultVariants.COMMON_ERROR,
                                        commonError = throwable.message
                                )
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

}