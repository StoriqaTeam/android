package com.storiqa.market.presentation.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.storiqa.market.R
import com.storiqa.market.di.navigation.NavScopeProvider
import com.storiqa.market.presentation.NavTabs
import com.storiqa.market.util.getThemedColor
import com.storiqa.market.util.log
import kotlinx.android.synthetic.main.activity_main.*
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import java.util.*


class NavigationActivity : MvpAppCompatActivity(), NavigationView {

    @InjectPresenter lateinit var presenter: NavigationPresenter
    private lateinit var callbackManager: CallbackManager

    @ProvidePresenter fun providePresenter() = NavScopeProvider.get().presenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callbackManager = CallbackManager.Factory.create()

        // todo move it from presenter
        login_button.setReadPermissions(Arrays.asList("email","public_profile","user_gender"))

        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                log("loginResult -> $ $loginResult")
            }

            override fun onCancel() {
                log("FB login onCancel()")
            }

            override fun onError(e: FacebookException) {
                log("FB login error -> $e")
            }
        })

        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        log("LoginManager res -> $loginResult")
                    }

                    override fun onCancel() {
                        log("LoginManager onCancel()")
                        // App code
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                        log("LoginManager error -> $exception")
                    }
                })

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        presenter.checkLocalAuth()
    }

    override fun onStop() {
        super.onStop()
        NavScopeProvider.close()
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

    override fun showErrorDetails(msg: String) {
        AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("I see") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
    }

    override fun showErrorDetails(msgRes: Int) {
        AlertDialog.Builder(this)
                .setMessage(msgRes)
                .setPositiveButton("I see") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
    }

    override fun indicateEmailError(msg: String?) {
        email_et.error = msg
    }

    override fun indicatePassError(msg: String?) {
        pass_et.error = msg
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
        bottom_navigation.setOnTabSelectedListener { position, _ ->
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
