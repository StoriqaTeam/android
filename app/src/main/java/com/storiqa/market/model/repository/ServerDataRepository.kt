package com.storiqa.market.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.storiqa.market.model.reponse.LangsResult
import com.storiqa.market.model.reponse.MarketServerResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ServerDataRepository constructor(
        private val client: ApolloClient
) {

    fun getMeInfo(): Single<Response<Me_Query.Data>> =
            Rx2Apollo.from<Me_Query.Data>(
                    client.query(
                            Me_Query
                                    .builder()
                                    .build()
                    )
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    fun getCurrencies(): Single<Response<Currencies_Query.Data>> =
            Rx2Apollo.from<Currencies_Query.Data>(
                    client.query(
                            Currencies_Query
                                    .builder()
                                    .build()
                    )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    fun getLanguages(): Single<LangsResult> =
            (Rx2Apollo.from<Languages_Query.Data>(
                    client.query(
                            Languages_Query
                                    .builder()
                                    .build()
                    )
            ))
                    .map { it ->
                        LangsResult(MarketServerResponse(it), null, null)
                    }
                    .onErrorReturn{ throwable ->
                        when (throwable) {
                            is ApolloNetworkException ->
                                LangsResult(null, null, "no connection to server")
                            else ->
                                LangsResult(null, null, throwable.message)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

}