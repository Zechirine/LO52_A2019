package com.startup42.equal.model

class ShareResponse {
    val error: String? = null
    val result: ShareResult? = null
}

class ShareResult {
    val userId: String = ""
    val firstName: String = ""
    val lastName: String = ""
    val verified: Boolean = false
}