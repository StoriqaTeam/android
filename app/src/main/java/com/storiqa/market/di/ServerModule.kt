package com.storiqa.market.di

import com.apollographql.apollo.ApolloClient
import com.storiqa.market.di.provider.ClientProvider
import com.storiqa.market.di.provider.OkHttpProvider
import com.storiqa.market.di.qualifier.BaseUrl
import com.storiqa.market.di.qualifier.Token
import okhttp3.OkHttpClient
import toothpick.config.Module

class ServerModule(baseUrl: String, token: String) : Module() {
    init {
        bind(String::class.java).withName(BaseUrl::class.java).toInstance(baseUrl)
        bind(String::class.java).withName(Token::class.java).toInstance(token)

        bind(OkHttpClient::class.java).toProvider(OkHttpProvider::class.java).providesSingletonInScope()
        bind(ApolloClient::class.java).toProvider(ClientProvider::class.java).providesSingletonInScope()
    }
}