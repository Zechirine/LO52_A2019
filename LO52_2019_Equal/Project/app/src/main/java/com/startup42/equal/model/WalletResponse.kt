package com.startup42.equal.model

class WalletResponse {
    val error: String? = null
    val result: WalletResult? = null
}

class WalletResult{
    val walletId: String = ""
    val title: String = ""
    val description: String = ""
    val members: ArrayList<WalletMembers>? = null
    var flux: ArrayList<WalletFlux>? = null
    val statistics: WalletStatistics? = null
    val balances: ArrayList<WalletBalance>? = null
}

class WalletStatistics{
    val expenses: Double = 0.0
    val receipts: Double = 0.0
    val total: Double = 0.0
}

class WalletMembers{

    val name : String = ""
    val userId : String? = null
    val balance : Double = 0.0
}

class WalletFlux{
    val fluxId: String = ""
    val title: String = ""
    val amount: Double = 0.0
    val from: ArrayList<WalletFluxMember>? = null
    val to: ArrayList<WalletFluxMember>? = null
    val status: String = ""//approved/declined/pending
}

class WalletFluxMember{
    val name: String = ""
    val userId: String? = null
    val from: Double = 0.0
    val to: Double = 0.0
    val balance: Double = 0.0
    val approved: Boolean = false
}

class WalletBalance {
    val from: String = ""
    val to: String = ""
    val amount: Double = 0.0
}