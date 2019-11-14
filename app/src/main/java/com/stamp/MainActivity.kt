package com.stamp

import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.stamp.viewModel.PassListViewMode
import com.stamp.viewModel.PassesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppBarActivity() {

    private lateinit var passes: PassesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(app_bar)

        passes = ViewModelProviders.of(this)[PassesViewModel::class.java]

        val sharedPref = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val code = sharedPref.getString(getString(R.string.saved_code), null)
        passes.setMode(PassListViewMode.CUSTOMER)

        val passesFragment = PassesFragment()
        val codeFragment = CodeFragment.newInstance(code)
        val cardsFragment = CardsFragment()
        loadFragment(passesFragment)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_bottom_navigation_passes -> {
                    loadFragment(passesFragment)
                    passes.getPasses()
                    true
                }

                R.id.main_bottom_navigation_code -> {
                    loadFragment(codeFragment)
                    true
                }

                R.id.main_bottom_navigation_cards -> {
                    loadFragment(cardsFragment)
                    true
                }

                else -> false
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .commit()
    }

}
