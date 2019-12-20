package com.startup42.equal.viewModel

import com.startup42.equal.service.FluxDeclineRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

class DeclineFluxViewModel {

    val fluxDeclineRequest = FluxDeclineRequest()

    fun sendData(parameters: HashMap<String, Any>): Observable<String> {
        return Observable.create { emitter ->
            fluxDeclineRequest.request(parameters).subscribeBy(
                onNext = {
                    if (it.error != null) {
                        emitter.onNext(it.error)
                    } else {
                        emitter.onNext("You just declined the flux")
                    }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }

            )
        }
    }
}