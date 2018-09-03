package com.storiqa.market.model.data.storage

import android.content.Context
import com.storiqa.market.model.data.auth.AuthHolder
import javax.inject.Inject

class Prefs @Inject constructor(
        private val context: Context
) : AuthHolder {
    private val AUTH_DATA = "auth_data"
    private val KEY_TOKEN = "ad_token"

    private fun getSharedPreferences(prefsName: String)
            = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    override var token: String?
        get() = getSharedPreferences(AUTH_DATA).getString(KEY_TOKEN, null)
        set(value) {
            getSharedPreferences(AUTH_DATA).edit().putString(KEY_TOKEN, value).apply()
        }

}