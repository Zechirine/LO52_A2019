package com.startup42.equal.viewModel

import android.content.Context
import com.startup42.equal.Equal
import com.startup42.equal.service.CreateWalletRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy


class CreateWalletViewModel {

    val createWalletRequest = CreateWalletRequest()

    fun sendData(parameters: HashMap<String, Any>): Observable<String> {
        return Observable.create { emitter ->
            createWalletRequest.request(parameters).subscribeBy(
                onNext = {
                    if (it.error != null) {
                        emitter.onNext(it.error)
                    } else {
                        emitter.onNext("Wallet created")
                    }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }
            )
        }
    }

}