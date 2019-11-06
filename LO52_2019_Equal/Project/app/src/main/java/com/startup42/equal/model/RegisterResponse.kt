package com.startup42.equal.model

class RegisterResponse {
    val error: String? = null
    val result: RegisterResult? = null
}

class RegisterResult {
    val userId: String= ""
    val token: String = ""
    val userTag: String = ""
}