package com.storiqa.market.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
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

    fun getLanguages(): Single<Response<Languages_Query.Data>> =
            (Rx2Apollo.from<Languages_Query.Data>(
                    client.query(
                            Languages_Query
                                    .builder()
                                    .build()
                    )
            ))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


}