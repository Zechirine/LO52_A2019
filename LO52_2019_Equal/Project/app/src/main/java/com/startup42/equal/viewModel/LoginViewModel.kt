package com.startup42.equal.viewModel

import android.content.Context
import com.startup42.equal.service.LoginRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.*
import com.startup42.equal.Equal

class LoginViewModel {

    val loginRequest = LoginRequest()

    fun sendData(parameters: HashMap<String, Any>): Observable<String> {
        return Observable.create { emitter ->
            loginRequest.request(parameters).subscribeBy(
                onNext = {
                    if (it.error != null) {
                        emitter.onNext(it.error)
                    } else {
                        val editor = Equal.context.getSharedPreferences("Login", Context.MODE_PRIVATE).edit()
                        editor.putString("token", it.result?.token)
                        editor.putString("userId", it.result?.userId)
                        editor.putString("userTag",it.result?.userTag)
                        editor.apply()
                        emitter.onNext("You are logged")
                    }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }
            )
        }
    }
}