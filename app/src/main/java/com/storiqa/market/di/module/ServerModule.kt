package com.storiqa.market.di.module

import com.apollographql.apollo.ApolloClient
import com.storiqa.market.BuildConfig
import com.storiqa.market.di.AppScope
import com.storiqa.market.domain.ApiClientInteractor
import com.storiqa.market.model.data.auth.AuthHolder
import com.storiqa.market.model.repository.ServerDataRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


@Module
class ServerModule {
    val BASE_URL = BuildConfig.endpoint

    @Provides
    @AppScope
    fun provideOkHttpClient(authHolder: AuthHolder): OkHttpClient {
        return OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(original.method(),
                            original.body())
                    builder.addHeader("Authorization", authHolder.token?: "")
                    builder.addHeader("Currency", "STQ")  //todo remove hardcode!!
                    chain.proceed(builder.build())
                }
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()
    }

    @Provides
    @AppScope
    fun provideApiClient (okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build()
    }

    @Provides
    @AppScope
    fun provideRepository(apolloClient: ApolloClient): ServerDataRepository {
        return ServerDataRepository(apolloClient)
    }

    @Provides
    @AppScope
    fun provideApiClientInteractor(repository: ServerDataRepository): ApiClientInteractor {
        return ApiClientInteractor(repository)
    }

}