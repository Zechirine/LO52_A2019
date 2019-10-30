package com.startup42.equal.service

import com.beust.klaxon.Klaxon
import com.startup42.equal.BuildConfig
import com.startup42.equal.model.HomeResponse
import io.reactivex.Observable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

class HomeRequest {

    private fun url(userId: String) : String{
        return BuildConfig.BASE_URL + "/dev/users/" + userId + "/wallets"
    }

    fun request(userId: String) : Observable<HomeResponse>{
        return Observable.create { emitter ->
            var client = OkHttpClient()
            var request = BaseRequest(client)

            request.GET(url(userId), object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    println("Activity Failure.")
                }

                override fun onResponse(call: Call?, response: Response) {
                    val responseData = response.body()?.string()
                    if (responseData != null) {
                        val result = Klaxon().parse<HomeResponse>(responseData)
                        result?.let { emitter.onNext(it) }
                    }
                }
            })
        }
    }
}