package com.storiqa.market.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import com.storiqa.market.model.reponse.ErrorDetails
import com.storiqa.market.model.reponse.MarketServException
import com.storiqa.market.model.reponse.MarketServerResponse
import com.storiqa.market.model.reponse.wrapThrowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ServerDataRepository constructor(
        private val client: ApolloClient
) {

    fun getMeInfo(): Single<Me_Query.Data> =
            Rx2Apollo.from<Me_Query.Data>(
                    client.query(
                            Me_Query
                                    .builder()
                                    .build()
                    )
            )
                    .onErrorResumeNext { t -> Single.error(wrapThrowable(t)) }
                    .map { it ->
                        val marketResp = MarketServerResponse(it)
                        val data = marketResp.successData
                        if (data != null) {
                            if (data.me() != null) {
                                data
                            } else {
                                throw MarketServException(
                                        ErrorDetails(payload = "have no [me] in data")
                                )
                            }
                        } else {
                            throw MarketServException(
                                    ErrorDetails(payload = marketResp.errorDetails.payload)
                            )
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    fun getCurrencies(): Single<Currencies_Query.Data> =
            Rx2Apollo.from<Currencies_Query.Data>(
                    client.query(
                            Currencies_Query
                                    .builder()
                                    .build()
                    )
            )
                    .onErrorResumeNext { t -> Single.error(wrapThrowable(t)) }
                    .map { it ->
                        val resp = MarketServerResponse(it)
                        if (resp.finalSuccess) {
                            resp.successData!!
                        } else {
                            throw MarketServException(resp.errorDetails )
                        }
                    }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    fun getLanguages(): Single<Languages_Query.Data> =
            Rx2Apollo.from<Languages_Query.Data>(
                    client.query(
                            Languages_Query
                                    .builder()
                                    .build()
                    )
            )
                    .onErrorResumeNext { t -> Single.error(wrapThrowable(t)) }
                    .map { it ->
                        val resp = MarketServerResponse(it)
                        if (resp.finalSuccess) {
                            resp.successData!!
                        } else {
                            throw MarketServException(resp.errorDetails)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


}