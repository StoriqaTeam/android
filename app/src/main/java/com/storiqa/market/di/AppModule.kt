package com.storiqa.market.di

import android.content.Context
import com.storiqa.market.model.data.auth.AuthHolder
import com.storiqa.market.model.data.storage.Prefs
import toothpick.config.Module

class AppModule(context: Context) : Module() {

    init {

        bind(Context::class.java).toInstance(context)
        //Auth
        bind(AuthHolder::class.java).to(Prefs::class.java).singletonInScope()

    }

}