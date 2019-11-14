package com.stamp.model

data class LoginResult(
    val success: LoggedInUser? = null,
    val error: Int? = null
)