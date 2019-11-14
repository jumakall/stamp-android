package com.stamp

import android.app.Application
import android.content.Context
import com.stamp.network.ApiAuthorizationInterceptor
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory

@Suppress("unused")
class StampApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize Sentry
        Sentry.init(AndroidSentryClientFactory(this))

        // set api token if available
        val sharedPref = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        sharedPref.getString(getString(R.string.saved_api_token), null)?.apply {
            ApiAuthorizationInterceptor.getInstance().setApiToken(this)
        }
    }

}