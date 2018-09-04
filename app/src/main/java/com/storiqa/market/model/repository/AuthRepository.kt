package com.storiqa.market.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import com.storiqa.market.model.reponse.MarketServerResponse
import com.storiqa.market.model.data.auth.AuthHolder
import com.storiqa.market.model.reponse.AuthErrorPayload
import com.storiqa.market.model.reponse.ErrorDetails
import com.storiqa.market.model.reponse.MarketException
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

    fun login(login: String, pass: String): Single<MarketServerResponse<Login_Mutation.Data>> =
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
                    .map { it -> MarketServerResponse(it) }
                    .flatMap { resp ->
                        if (resp.finalSuccess) {
                            return@flatMap Single.just(resp)
                        } else {
                            val str = resp.errorDetails.payload
                            val authPayload = str?.let { AuthErrorPayload(str) }

                            authPayload?.let {
                                val newDetails = ErrorDetails(
                                        resp.errorDetails.errorCode, authPayload.toString())
                                throw MarketException(newDetails)
                            }

                            throw MarketException(resp.errorDetails)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

}