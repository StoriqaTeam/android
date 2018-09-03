package com.storiqa.market.di

import com.storiqa.market.MarketApp
import com.storiqa.market.di.module.AppModule
import com.storiqa.market.di.module.AuthModule
import com.storiqa.market.di.module.ServerModule
import com.storiqa.market.di.navigation.NavComponent
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@AppScope
@Component(modules = [AppModule::class, ServerModule::class, AuthModule::class])
interface AppComponent {

    fun navComponentBuilder(): NavComponent.Builder

}

object AppScopeProvider {

    private lateinit var appComponent: AppComponent

    /**
     * Open top level scope in
     * @param app onCreate()
     */
    fun open(app: MarketApp) {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
    }

    fun get(): AppComponent = appComponent
}