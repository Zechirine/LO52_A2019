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
                        val editor = Equal.context.getSharedPreferences("CreateWallet", Context.MODE_PRIVATE).edit()
                        editor.putString("title" ,it.result?.title)
                        editor.putString("owner" ,it.result?.owner)
                        editor.putString("description",it.result?.description)
                        editor.putInt("members",it.result?.members!!)
                        editor.putString("me",it.result?.me)
                        editor.apply()
                        emitter.onNext("Wallet created")
                    }
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }
            )
        }
    }

}