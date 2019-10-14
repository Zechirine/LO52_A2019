package com.startup42.equal.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.startup42.equal.R
import com.startup42.equal.viewModel.LoginViewModel
import com.startup42.equal.viewModel.RegisterViewModel
import io.reactivex.rxkotlin.*

import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    val viewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            if (!emailInput.text.isEmpty() and !passwordInput.text.isEmpty()) {

                val hashMap = HashMap<String, Any>()
                hashMap.put("email", emailInput.text.toString())
                hashMap.put("password", passwordInput.text.toString())




                viewModel.sendData(hashMap)
                    .doOnNext {

                        if (it == "You are logged") {
                            println(it)
                            val prefs = getSharedPreferences("Login", Context.MODE_PRIVATE)

                            //TODO redirect

                            println("Token  : " + prefs.getString("token", "errortoken"))
                            println("UserId : " + prefs.getString("userId", "errorid"))
                        } else {
                            this.runOnUiThread{
                                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    .doOnError { it.printStackTrace() }
                    .doOnComplete{ println("onComplete!") }
                    .subscribe()

            } else {
                //TODO set string
                Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show();
            }
        }

        notRegisteredYetLink.setOnClickListener{

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }
    }

}
