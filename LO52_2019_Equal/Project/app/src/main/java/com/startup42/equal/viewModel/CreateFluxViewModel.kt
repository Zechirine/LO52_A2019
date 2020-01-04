package com.startup42.equal.viewModel

import com.startup42.equal.model.CreateFluxResult
import com.startup42.equal.service.CreateFluxRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

class CreateFluxViewModel {

    val createFluxRequest = CreateFluxRequest()

    fun sendData(parameters: HashMap<String, Any>): Observable<String> {
        return Observable.create { emitter ->
            createFluxRequest.request(parameters).subscribeBy(
                onNext = {
                    if (it.error != null) {
                        emitter.onNext(it.error)
                    } else {
                        emitter.onNext("Flux created")
                    }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }
            )
        }
    }
}