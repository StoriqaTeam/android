package com.storiqa.market.presentation.main

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.storiqa.market.R
import com.storiqa.market.di.DI
import com.storiqa.market.presentation.NavTabs
import com.storiqa.market.util.getThemedColor
import com.storiqa.market.util.log
import kotlinx.android.synthetic.main.activity_main.*
import toothpick.Toothpick


class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        return Toothpick
                .openScope(DI.SERVER_SCOPE)
                .getInstance(MainPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        populateBottomMenu()
        setNavListener()

        test_bt.setOnClickListener { presenter.onGetLangsClicked() }

        me_bt.setOnClickListener{ presenter.onMeInfoRequested() }

        login_bt.setOnClickListener { presenter.onLogin(
                email_et.text.toString(), pass_et.text.toString()
        ) }

        logout_bt.setOnClickListener { presenter.onLogout() }
    }

    override fun onStart() {
        super.onStart()
        presenter.checkLoclAuth()
    }

    override fun showLangsText(text: String) {
        langs_tv.text = text
    }

    override fun showMeInfo(text: String) {
        me_tv.text = text
    }

    override fun hideLoginView() {
        login_ll.visibility = View.GONE
        logout_bt.visibility = View.VISIBLE
    }

    override fun showLoginView() {
        login_ll.visibility = View.VISIBLE
        logout_bt.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        //todo clear Where should I close App scope, now closing in in MainActivity
        Toothpick.closeScope(DI.APP_SCOPE)
    }

    private fun populateBottomMenu() {
        // Create items
        val itemHome = AHBottomNavigationItem(getString(R.string.title_home), R.drawable.ic_home_black_24dp)
        val itemSearch = AHBottomNavigationItem(getString(R.string.title_search), R.drawable.ic_home_black_24dp)
        val itemBasket = AHBottomNavigationItem(getString(R.string.title_basket), R.drawable.ic_home_black_24dp)
        val itemOrders = AHBottomNavigationItem(getString(R.string.title_orders), R.drawable.ic_home_black_24dp)
        val itemMore = AHBottomNavigationItem(getString(R.string.title_more), R.drawable.ic_home_black_24dp)

        // Add items
        bottom_navigation.addItem(itemHome)
        bottom_navigation.addItem(itemSearch)
        bottom_navigation.addItem(itemBasket)
        bottom_navigation.addItem(itemOrders)
        bottom_navigation.addItem(itemMore)

        bottom_navigation.accentColor = getThemedColor(R.color.royal)
    }

    private fun setNavListener() {
        bottom_navigation.setOnTabSelectedListener { position, wasSelected ->
            log(position.toString())
            when (position) {
                NavTabs.TAB_HOME.ordinal -> message.setText(R.string.title_home)
                NavTabs.TAB_SEARCH.ordinal -> message.setText(R.string.title_search)
                NavTabs.TAB_MORE.ordinal -> message.setText(R.string.title_more)
            }
            true
        }
    }

}
