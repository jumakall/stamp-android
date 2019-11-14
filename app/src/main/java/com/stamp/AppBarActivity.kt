package com.stamp

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.stamp.network.ApiAuthorizationInterceptor

abstract class AppBarActivity : AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_logout -> {
            val sharedPref = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

            // clear shared preferences
            with(sharedPref.edit()) {
                clear()
                apply()
            }

            // remove api token from the interceptor
            ApiAuthorizationInterceptor.getInstance().setApiToken(null)

            startActivity(
                Intent(this, SplashActivity::class.java)
            )

            finish()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}