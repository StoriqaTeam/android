package com.storiqa.market.di.provider

import com.apollographql.apollo.ApolloClient
import com.storiqa.market.di.qualifier.BaseUrl
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Provider

class ClientProvider @Inject constructor(
        private val okHttpClient: OkHttpClient,
        @BaseUrl private val url: String
): Provider<ApolloClient> {
    override fun get(): ApolloClient {
        return ApolloClient.builder()
                .serverUrl(url)
                .okHttpClient(okHttpClient)
                .build()
    }

}