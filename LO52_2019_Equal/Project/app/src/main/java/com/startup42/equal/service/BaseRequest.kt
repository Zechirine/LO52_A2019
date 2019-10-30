package com.startup42.equal.service

import android.content.Context
import okhttp3.*
import java.util.HashMap
import com.google.gson.GsonBuilder
import com.startup42.equal.Equal


class BaseRequest(client: OkHttpClient) {

    internal var client = OkHttpClient()

    init {
        this.client = client
    }

    internal var gson = GsonBuilder()
        .serializeNulls()
        .disableHtmlEscaping()
        .create()

    var token = Equal.context.getSharedPreferences("Login", Context.MODE_PRIVATE).
        getString("token","Unauthorized")

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
            .addHeader("Authorization","Bearer " + token)
            .post(body)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun GET(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer " + token)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
}