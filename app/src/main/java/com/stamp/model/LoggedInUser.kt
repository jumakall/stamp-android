package com.stamp.model

data class LoggedInUser (
    val api_token: String,
    val name: String,
    val is_vendor: Boolean,
    val code: String
)