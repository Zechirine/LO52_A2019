package com.startup42.equal.service

import com.beust.klaxon.Klaxon
import com.startup42.equal.BuildConfig
import com.startup42.equal.model.WalletResponse
import io.reactivex.Observable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

class WalletRequest {

    private fun url(walletId: String) : String{
        return BuildConfig.BASE_URL + "/dev/wallets/"+walletId
    }

    fun request(walletId: String) : Observable<WalletResponse> {
        return Observable.create { emitter ->
            var client = OkHttpClient()
            var request = BaseRequest(client)

            request.GET(url(walletId), object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    println("Activity Failure.")
                }

                override fun onResponse(call: Call?, response: Response) {
                    val responseData = response.body()?.string()
                    if (responseData != null) {
                        val result = Klaxon().parse<WalletResponse>(responseData)
                        result?.let { emitter.onNext(it) }
                    }
                }
            })
        }
    }

}