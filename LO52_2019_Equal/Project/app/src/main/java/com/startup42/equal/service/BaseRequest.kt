package com.startup42.equal.service

import okhttp3.*
import java.util.HashMap
import com.google.gson.GsonBuilder


class BaseRequest(client: OkHttpClient) {

    internal var client = OkHttpClient()

    init {
        this.client = client
    }

    internal var gson = GsonBuilder()
        .serializeNulls()
        .disableHtmlEscaping()
        .create()

    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }

    fun unAuthenticatedPOST(url: String, parameters: HashMap<String,Any>, callback: Callback): Call {
        val json = gson.toJson(parameters)
        var body = RequestBody.create(JSON, json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()


        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun POST(url: String, parameters: HashMap<String,Any>, callback: Callback): Call {

        val json = gson.toJson(parameters)
        var body = RequestBody.create(JSON, json)
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer"/* + Token*/)
            .post(body)
            .build()


        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun GET(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer"/* + Token*/)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
}