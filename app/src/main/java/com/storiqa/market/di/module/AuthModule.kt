package com.storiqa.market.di.module

import com.apollographql.apollo.ApolloClient
import com.storiqa.market.MarketApp
import com.storiqa.market.di.AppScope
import com.storiqa.market.domain.AuthInteractor
import com.storiqa.market.model.data.auth.AuthHolder
import com.storiqa.market.model.data.storage.Prefs
import com.storiqa.market.model.repository.AuthRepository
import dagger.Module
import dagger.Provides


@Module
class AuthModule {

    @Provides
    @AppScope
    fun provideAuthHolder(application: MarketApp): AuthHolder {
        return Prefs(application)
    }

    @Provides
    @AppScope
    fun provideAuthRepository(authHolder: AuthHolder, apolloClient: ApolloClient): AuthRepository {
        return AuthRepository(authHolder, apolloClient)
    }

    @Provides
    @AppScope
    fun provideAuthInteractor(repository: AuthRepository): AuthInteractor {
        return AuthInteractor(repository)
    }

}