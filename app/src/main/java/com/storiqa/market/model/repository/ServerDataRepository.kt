package com.storiqa.market.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.rx2.Rx2Apollo
import com.storiqa.market.model.reponse.ErrorDetails
import com.storiqa.market.model.reponse.MarketServerResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ServerDataRepository constructor(
        private val client: ApolloClient
) {

    fun getMeInfo(): Single<MarketServerResponse<Me_Query.Data>> =
            Rx2Apollo.from<Me_Query.Data>(
                    client.query(
                            Me_Query
                                    .builder()
                                    .build()
                    )
            )
                    .map { it -> MarketServerResponse(it) }
                    .onErrorReturn { throwable ->
                        wrapThrowable(throwable) as MarketServerResponse<Me_Query.Data>
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    fun getCurrencies(): Single<MarketServerResponse<Currencies_Query.Data>> =
            Rx2Apollo.from<Currencies_Query.Data>(
                    client.query(
                            Currencies_Query
                                    .builder()
                                    .build()
                    )
            )
                    .map { it -> MarketServerResponse(it) }
                    .onErrorReturn { throwable ->
                        wrapThrowable(throwable) as MarketServerResponse<Currencies_Query.Data>
                    }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    fun getLanguages(): Single<MarketServerResponse<Languages_Query.Data>> =
            (Rx2Apollo.from<Languages_Query.Data>(
                    client.query(
                            Languages_Query
                                    .builder()
                                    .build()
                    )
            ))
                    .map { it ->  MarketServerResponse(it) }
                    .onErrorReturn { t ->
                        wrapThrowable(t) as MarketServerResponse<Languages_Query.Data>}
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    private fun wrapThrowable(throwable: Throwable): MarketServerResponse<Operation.Data> {
        return when (throwable) {
            is ApolloNetworkException ->
                MarketServerResponse(
                        ErrorDetails(
                                payload = "no connection to server"
                        )
                )
            else -> {
                val exMsg = throwable.message
                MarketServerResponse(
                        if (exMsg == null) {
                            ErrorDetails()
                        } else {
                            ErrorDetails(
                                    payload = exMsg
                            )
                        }
                )
            }
        }
    }
}