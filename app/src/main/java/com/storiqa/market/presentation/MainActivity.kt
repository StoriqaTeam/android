package com.storiqa.market.presentation

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.storiqa.market.R
import com.storiqa.market.util.getThemedColor
import com.storiqa.market.util.log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            log("presenter -> " + presenter.toString())
        } catch (error: NullPointerException) {
            log("presenter is null")
        }

        bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        populateBottomMenu()
        setNavListener()
    }

    override fun onStart() {
        super.onStart()
        presenter.onReadyToShow()
    }

    override fun showCurrenciesText(text: String) {
        message.text = text
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
