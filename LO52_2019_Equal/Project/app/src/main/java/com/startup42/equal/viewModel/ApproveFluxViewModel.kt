package com.startup42.equal.viewModel

import com.startup42.equal.service.FluxApproveRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

class ApproveFluxViewModel {

    val fluxApproveRequest = FluxApproveRequest()

    fun sendData(parameters: HashMap<String, Any>): Observable<String> {
        return Observable.create { emitter ->
            fluxApproveRequest.request(parameters).subscribeBy(
                onNext = {
                    if (it.error != null) {
                        emitter.onNext(it.error)
                    } else {
                        emitter.onNext("You just approved the flux")
                    }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }

            )
        }
    }
}