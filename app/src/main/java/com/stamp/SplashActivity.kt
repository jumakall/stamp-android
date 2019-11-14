package com.stamp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stamp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        if (sharedPref.contains(getString(R.string.saved_api_token))) {
            val isVendor = sharedPref.getBoolean(getString(R.string.saved_is_vendor), false)

            startActivity(
                Intent(
                    this,
                    if(isVendor) VendorActivity::class.java else MainActivity::class.java
                )
            )
            finish()
        }

        setContentView(R.layout.activity_splash)
    }

}
