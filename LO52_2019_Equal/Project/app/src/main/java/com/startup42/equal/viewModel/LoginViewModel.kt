package com.startup42.equal.viewModel

import com.startup42.equal.service.LoginRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.*

class LoginViewModel {

    val loginRequest = LoginRequest()

    fun sendData(parameters: HashMap<String, Any>): Observable<String> {
        return Observable.create { emitter ->
            loginRequest.request(parameters).subscribeBy(
                onNext = {
                    if (it.error != null) {
                        emitter.onNext(it.error)
                    } else {
                        /*TODO put all data in preferences*/
                        emitter.onNext("You are logged")
                    }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }

            )
        }
    }
}