package com.startup42.equal.viewModel

import com.startup42.equal.service.ShareRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

class ShareViewModel {

    val shareRequest = ShareRequest()

    fun sendData(parameters: HashMap<String, Any>): Observable<String> {
        return Observable.create { emitter ->
            shareRequest.request(parameters).subscribeBy(
                onNext = {
                    if (it.error != null) {
                        println("ERRROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOORR")
                        emitter.onNext(it.error)
                    } else {
                        emitter.onNext("The wallet was correctly shared")
                    }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }
            )
        }
    }
}