package com.startup42.equal.model

class HomeResponse {
    val error: String? = null
    val result: ArrayList<HomeResult>? = null
}

class HomeResult {
    val walletId: String? = null
    val title: String? = null
    val description: String? = null
    val members: Int? = null
    val shared: Int? = null
    val userBalance: Double? = null
}