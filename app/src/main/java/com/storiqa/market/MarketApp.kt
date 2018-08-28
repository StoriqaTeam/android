package com.storiqa.market

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.storiqa.market.di.AppModule
import com.storiqa.market.di.DI
import com.storiqa.market.di.ServerModule
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

class MarketApp : Application() {
    lateinit var client: ApolloClient

    override fun onCreate() {
        super.onCreate()

        initToothpick()
        initAppScope()

    }

    private fun initToothpick() {

        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
            FactoryRegistryLocator.setRootRegistry(com.storiqa.market.FactoryRegistry())
            MemberInjectorRegistryLocator.setRootRegistry(com.storiqa.market.MemberInjectorRegistry())
        }
    }

    private fun initAppScope() {
        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(AppModule(this))

        val serverScope = Toothpick.openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
        serverScope.installModules(
                ServerModule(
                        "https://nightly.stq.cloud:60443/graphql",
                        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJleHAiOjE1MzU0NTc2OTMsInByb3ZpZGVyIjoiRW1haWwifQ.FPGoadYqZmtubXGXe71BlX9JzpG3oWQBu3etFotgkRQtvFgvUusGlUvvfqzbgxxMGXWilG7ohwepzghn2yhkeV1lSeSqRvS9jKu5Wawi3o0byZaymNLRnjaJ-OWiTztzGqK9MuEyscPVACpd-KhoFGNaLX-QKfNyO4ymIHarx8L9Vhhuob_TXZddcYiirawl2yv8R7GX8lR0bNZMRvjkNRP7XraCHtOe0VHf-YfBT0Wub8VZ78Jt-fsKaQ4YXIn1ol2z1DU-8VefoR4Sb5qXyhSepilgOsTz19zTwcCCXxDqzghTPDM9cK6dQShYIqu17OtZbx83PZVfQzKUxPm76Q"
                        ))

    }



}