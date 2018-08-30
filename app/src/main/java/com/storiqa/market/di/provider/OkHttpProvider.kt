package com.storiqa.market.di.provider

import com.storiqa.market.model.data.auth.AuthHolder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Provider

class OkHttpProvider @Inject constructor(
        private val authHolder: AuthHolder
) : Provider<OkHttpClient> {
    override fun get(): OkHttpClient {
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
}