package com.startup42.equal.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.startup42.equal.R
import com.startup42.equal.viewModel.LoginViewModel

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

                loginButton.isEnabled = false
                viewModel.sendData(hashMap)
                    .doOnNext {

                        if (it == "You are logged") {
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        } else {
                            this.runOnUiThread{
                                loginButton.isEnabled = true
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
