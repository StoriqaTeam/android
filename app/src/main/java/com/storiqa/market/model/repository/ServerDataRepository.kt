package com.storiqa.market.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ServerDataRepository @Inject constructor(
        private val client: ApolloClient
) {

    fun getMeInfo() =
            Rx2Apollo.from<Me_Query.Data>(
                    client.query(
                            Me_Query
                                    .builder()
                                    .build()
                    )
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    fun getCurrencies() =
            Rx2Apollo.from<Currencies_Query.Data>(
                    client.query(
                            Currencies_Query
                                    .builder()
                                    .build()
                    )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    fun getLanguages() =
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