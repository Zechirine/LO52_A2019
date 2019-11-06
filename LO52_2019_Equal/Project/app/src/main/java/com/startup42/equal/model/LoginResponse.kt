package com.startup42.equal.model

class LoginResponse {
    val error: String? = null
    val result: LoginResult? = null
}

class LoginResult {
    val userId: String= ""
    val token: String = ""
    val userTag: String = ""
}