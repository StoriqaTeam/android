package com.storiqa.market

import android.app.Application
import com.storiqa.market.di.AppScopeProvider

class MarketApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppScopeProvider.open(this)

    }
}