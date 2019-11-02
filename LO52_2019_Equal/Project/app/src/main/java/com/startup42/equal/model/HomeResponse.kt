package com.startup42.equal.model

class HomeResponse {
    val error: String? = null
    val result: ArrayList<HomeResult>? = null
}

class HomeResult {
    val walletId: String = ""
    val title: String = ""
    val description: String = ""
    val members: Int = 0
    val shared: Int = 0
    val userBalance: Double = 0.0
}