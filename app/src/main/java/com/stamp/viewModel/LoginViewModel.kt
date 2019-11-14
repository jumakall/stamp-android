package com.stamp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stamp.model.LoginFormState
import com.stamp.model.LoginResult

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginForm: LiveData<LoginFormState>
        get() = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    fun login(username: String, password: String) {
        // todo: implement login
    }

    fun loginDataChanged(username: String, password: String) {
        // todo: implement validation
    }

}