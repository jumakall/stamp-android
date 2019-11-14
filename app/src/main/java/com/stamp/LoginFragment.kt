package com.stamp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.stamp.databinding.FragmentLoginBinding
import com.stamp.model.LoggedInUser
import com.stamp.model.LoginCredentials
import com.stamp.network.ApiAuthorizationInterceptor
import com.stamp.network.StampApi
import com.stamp.viewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    private val loginJob = Job()
    private val coroutineScope = CoroutineScope(loginJob + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.login.setOnClickListener{ login() }

        binding.appBar.title = getString(R.string.login_title)

        return binding.root
    }

    private fun loggedIn(user: LoggedInUser) {
        // persist data to shared preferences
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        if (sharedPref != null) {
            with(sharedPref.edit()) {
                putString(getString(R.string.saved_api_token), user.api_token)
                putString(getString(R.string.saved_name), user.name)
                putBoolean(getString(R.string.saved_is_vendor), user.is_vendor)
                putString(getString(R.string.saved_code), user.code)
                apply()
            }
        }

        // set api token for network requests
        ApiAuthorizationInterceptor.getInstance().setApiToken(user.api_token)

        startActivity(
            Intent(
                activity,

                if (user.is_vendor)
                    VendorActivity::class.java
                else
                    MainActivity::class.java
            )
        )

        activity?.finish()
    }

    private fun login() {
        coroutineScope.launch {
            enableControls(false)

            val deferred = StampApi.retrofitService.getLoginAsync(
                LoginCredentials(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            )

            try {
                loggedIn(deferred.await())
            } catch (e: Exception) {
                Snackbar.make(binding.container, getString(R.string.login_failed), Snackbar.LENGTH_SHORT).show()
                enableControls(true)
            }
        }
    }

    private fun enableControls(enabled: Boolean)
    {
        binding.apply {
            username.isEnabled = enabled
            password.isEnabled = enabled
            login.isEnabled = enabled
            loading.visibility = if (enabled) View.GONE else View.VISIBLE
        }
    }
}
