package com.storiqa.market.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import com.storiqa.market.model.ErrorCode
import com.storiqa.market.model.ServerResponse
import com.storiqa.market.model.data.auth.AuthHolder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import type.CreateJWTEmailInput

class AuthRepository constructor(
        private val authData: AuthHolder,
        private val client: ApolloClient
) {

    fun isLogged() = !authData.token.isNullOrEmpty()

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
                                                        .build())
                                        .build()
                        )
                ))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

    fun saveToken(token: String) {
        authData.token = token
    }

    fun logout() {
        authData.token = null
    }


    fun loginAlt(login: String, pass: String): Single<ServerResponse<Login_Mutation.Data>> =
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
                    .subscribeOn(Schedulers.io())
                    .flatMap { it ->
                        val resp = ServerResponse(it.data(), it.errors())
                        if (resp.errorDetails.errorCode == ErrorCode.NO_ERROR.code) {
                            return@flatMap Single.just(resp)
                        } else {
                            throw NullPointerException("clarify it!")
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())

}