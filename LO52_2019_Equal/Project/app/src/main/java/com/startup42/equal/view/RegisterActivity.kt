package com.startup42.equal.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.startup42.equal.viewModel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern
import com.startup42.equal.R
import java.util.regex.Matcher


class RegisterActivity : AppCompatActivity() {

    val viewModel = RegisterViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        registerButton.setOnClickListener {

            //Check if all field are filled
            if (!emailInput.text.isEmpty() and !passwordInput.text.isEmpty() and !lastNameInput.text.isEmpty() and !firstnameInput.text.isEmpty() and !confirmPasswordInput.text.isEmpty()) {

                //Check if mail address format is valid or not
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString()).matches()) {

                    //Check if password is matching with the verification
                    if (passwordInput.text.toString() == confirmPasswordInput.text.toString()) {

                        //Check if password is valid or not
                        if (isValidPassword(passwordInput.text.toString())) {
                            val hashMap = HashMap<String, Any>()
                            hashMap.put("firstName", firstnameInput.text.toString())
                            hashMap.put("lastName", lastNameInput.text.toString())
                            hashMap.put("email", emailInput.text.toString())
                            hashMap.put("password", passwordInput.text.toString())
                            hashMap.put("confirmPassword", confirmPasswordInput.text.toString())

                            viewModel.sendData(hashMap)
                                .doOnNext {

                                    if (it == "You are registered") {
                                        println(it)
                                        val prefs =
                                            getSharedPreferences("Login", Context.MODE_PRIVATE)

                                        //TODO redirect

                                        println(
                                            "Token  : " + prefs.getString(
                                                "token",
                                                "errortoken"
                                            )
                                        )
                                        println("UserId : " + prefs.getString("userId", "errorid"))
                                    } else {
                                        this.runOnUiThread {
                                            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                .doOnError { it.printStackTrace() }
                                .doOnComplete { println("onComplete!") }
                                .subscribe()

                            Toast.makeText(
                                this,
                                R.string.accountCreatedConfirmation,
                                Toast.LENGTH_LONG
                            ).show()


                            //Back to login activity
                            finish()

                        } else {

                            //Password not enough strong
                            Toast.makeText(
                                this,
                                R.string.passwordNotEnoughStrong,
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    } else {
                        //Password and confirm password are different
                        Toast.makeText(this, R.string.confirmAndPasswordError, Toast.LENGTH_LONG)
                            .show()

                    }

                } else {
                    //Mail address format is not valid
                    Toast.makeText(this, R.string.mailAddressFormatError, Toast.LENGTH_LONG).show()
                }

            } else {

                //Some fields are empty
                Toast.makeText(this, R.string.allFieldAreRequired, Toast.LENGTH_LONG).show()
            }

        }

        backButton.setOnClickListener {

            finish()
        }
    }

    fun isValidPassword(password: String): Boolean {

        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{7,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)

        return matcher.matches()

    }

    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

}