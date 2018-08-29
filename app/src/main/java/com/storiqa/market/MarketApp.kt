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
                ServerModule(BuildConfig.endpoint))

    }


}