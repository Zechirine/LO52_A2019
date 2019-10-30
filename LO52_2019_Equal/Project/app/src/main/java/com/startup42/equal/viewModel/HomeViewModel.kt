package com.startup42.equal.viewModel

import io.reactivex.Observable
import io.reactivex.rxkotlin.*
import com.startup42.equal.model.HomeResult
import com.startup42.equal.service.HomeRequest


class HomeViewModel {

    val homeRequest = HomeRequest()

    fun receiveData(userId: String): Observable<ArrayList<HomeResult>> {
        return Observable.create { emitter ->
            homeRequest.request(userId).subscribeBy(
                onNext = {
                    it.result?.let { result -> emitter.onNext(result) }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }
            )
        }
    }
}