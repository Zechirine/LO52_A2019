package com.startup42.equal.viewModel
import com.startup42.equal.model.WalletResult
import com.startup42.equal.service.WalletRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

class WalletViewModel {

    val walletRequest = WalletRequest()

    fun receiveData(walletId: String): Observable<WalletResult> {
        return Observable.create { emitter ->
            walletRequest.request(walletId).subscribeBy(
                onNext = {
                    it.result?.let { result -> emitter.onNext(result) }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }
            )
        }
    }
}