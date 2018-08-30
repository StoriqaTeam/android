package com.storiqa.market.di

import com.apollographql.apollo.ApolloClient
import com.storiqa.market.di.provider.ClientProvider
import com.storiqa.market.di.provider.OkHttpProvider
import com.storiqa.market.di.qualifier.BaseUrl
import com.storiqa.market.domain.AuthInteractor
import com.storiqa.market.domain.ClientInteractor
import com.storiqa.market.model.repository.AuthRepository
import com.storiqa.market.model.repository.ServerDataRepository
import okhttp3.OkHttpClient
import toothpick.config.Module

class ServerModule(baseUrl: String) : Module() {
    init {
        bind(String::class.java).withName(BaseUrl::class.java).toInstance(baseUrl)

        bind(OkHttpClient::class.java).toProvider(OkHttpProvider::class.java).providesSingletonInScope()
        bind(ApolloClient::class.java).toProvider(ClientProvider::class.java).providesSingletonInScope()

        bind(AuthRepository::class.java).singletonInScope()
        bind(AuthInteractor::class.java).singletonInScope()

        bind(ServerDataRepository::class.java).singletonInScope()
        bind(ClientInteractor::class.java).singletonInScope()

    }
}