package com.storiqa.market.di.module

import com.storiqa.market.MarketApp
import com.storiqa.market.di.AppScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: MarketApp) {

    @Provides
    @AppScope
    fun provideApplication(): MarketApp {
        return application
    }

}