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
import type.CreateJWTProviderInput
import type.Provider

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

    @Throws(MarketServException::class, AuthException::class)
    fun login(login: String, pass: String): Single<Login_Mutation.Data> =
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
                    .onErrorResumeNext { t ->
                        Single.error(wrapThrowable(t))
                    }
                    .map { it ->
                        val resp = MarketServerResponse(it)
                        if (resp.finalSuccess && !resp.successData!!.jwtByEmail.token().isEmpty()) {
                            resp.successData
                        } else {
                            throw AuthException (
                                    AuthErrorPayload(resp.errorDetails.payload)
                            )
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
    fun login(login: String, pass: String): Single<Response<Login_Mutation.Data>> =
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
                                                        .build()
                                        )
                                        .build()
                        )
                ))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

    fun loginWithFB(token: String): Single<Response<LoginProviderMutation.Data>> =
            Rx2Apollo.from<LoginProviderMutation.Data>(
                    client.mutate(
                            LoginProviderMutation
                                    .builder()
                                    .input(
                                            CreateJWTProviderInput
                                                    .builder()
                                                    .clientMutationId("")
                                                    .provider(Provider.FACEBOOK)
                                                    .token(token)
                                                    .build()
                                    )
                                    .build()
                    )
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

}