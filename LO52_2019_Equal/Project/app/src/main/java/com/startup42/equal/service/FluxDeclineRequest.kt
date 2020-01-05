package com.startup42.equal.service

import com.beust.klaxon.Klaxon
import com.startup42.equal.BuildConfig
import com.startup42.equal.model.FluxResponse
import io.reactivex.Observable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.util.HashMap

class FluxDeclineRequest {

    private fun url(): String {
        return BuildConfig.BASE_URL + "/dev/flux/decline"
    }

    fun request(parameters: HashMap<String, Any>): Observable<FluxResponse> {
        return Observable.create { emitter ->
            var client = OkHttpClient()
            var request = BaseRequest(client)

            request.POST(url(), parameters, object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    println("Activity Failure.")
                }

                override fun onResponse(call: Call?, response: Response) {
                    val responseData = response.body()?.string()
                    if (responseData != null) {
                        val result = Klaxon().parse<FluxResponse>(responseData)
                        result?.let { emitter.onNext(it) }
                    }
                }
            })
        }
    }

}