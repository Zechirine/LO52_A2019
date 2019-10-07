package com.startup42.equal.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.startup42.equal.R
import com.startup42.equal.viewModel.LoginViewModel
import io.reactivex.rxkotlin.*

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val viewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)

        /** TODO remove hashmap when activity done */
        val hashMap = HashMap<String, Any>()
        hashMap.put("email", "alban.pezzoli@gmail.com")
        hashMap.put("password", "Test1234")

        viewModel.sendData(hashMap).subscribeBy(
            onNext = { println(it)},
            onError = { it.printStackTrace() },
            onComplete = { println("onComplete!") }

        )
    }

}
