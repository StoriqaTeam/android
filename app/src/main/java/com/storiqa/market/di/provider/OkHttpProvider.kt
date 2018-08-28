package com.storiqa.market.di.provider

import com.storiqa.market.di.qualifier.Token
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Provider

class OkHttpProvider @Inject constructor(
        @Token private val token: String
) : Provider<OkHttpClient> {
    override fun get(): OkHttpClient {
        return OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(original.method(),
                            original.body())
                    builder.addHeader("Authorization", token)
                    chain.proceed(builder.build())
                }
                .build()
    }
}