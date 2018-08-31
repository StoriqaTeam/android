package com.storiqa.market.di.navigation

import android.support.v4.app.FragmentManager
import com.storiqa.market.di.AppScopeProvider
import com.storiqa.market.util.ScopeProvider
import com.storiqa.market.presentation.main.NavigationActivity
import com.storiqa.market.presentation.main.NavigationPresenter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class NavScope

@Module
abstract class NavModule constructor(val activity: NavigationActivity){
    @Provides
    fun provideSupportFragmentManager(): FragmentManager {
        return activity.getSupportFragmentManager() // todo Use by constructing navigation
    }
}

@Subcomponent(modules = [NavModule::class])
@NavScope
interface NavComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): NavComponent
    }

    fun inject(activity: NavigationActivity)
    fun presenter(): NavigationPresenter

}

object NavScopeProvider : ScopeProvider<NavComponent>() {
    override fun open(): NavComponent {
        return set(component ?: AppScopeProvider.get().navComponentBuilder().build())
    }
}