package com.stamp

import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_vendor.*

class VendorActivity : AppBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor)
        setSupportActionBar(app_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.vendor_activity_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
