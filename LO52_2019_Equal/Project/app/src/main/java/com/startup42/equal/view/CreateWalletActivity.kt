package com.startup42.equal.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.startup42.equal.Equal
import com.startup42.equal.R
import com.startup42.equal.viewModel.CreateWalletViewModel
import kotlinx.android.synthetic.main.activity_createwallet.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.row_createwallet.view.*


class CreateWalletActivity : AppCompatActivity() {

    val viewModel = CreateWalletViewModel()


    private fun setupToolBar() {

        titleTextView.setTextColor(getResources().getColor(R.color.textColorOnMainColor))
        titleTextView.text = getResources().getText(R.string.newwallet)

        backButton.setColorFilter(
            ContextCompat.getColor(this, R.color.textColorOnMainColor),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createwallet)

        setSupportActionBar(customToolbar)
        setupToolBar()

        //Add new Member
        addMemberButton.setOnClickListener {

            //Create the new member view (row_createwallet)
            val view = LayoutInflater.from(Equal.context)
                .inflate(com.startup42.equal.R.layout.row_createwallet, null, false)

            //Add Listener on the check box to check if just 1 checkbox is true
            view.ItsMeInput.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    for (i in 0..InputVerticalList.childCount) {

                        if (InputVerticalList.getChildAt(i) != view) {
                            try {
                                InputVerticalList.getChildAt(i).ItsMeInput.isChecked = false
                            } catch (e: Exception) {}
                        }
                    }

                }
            }

            //Add view to scrollView
            InputVerticalList.addView(view, InputVerticalList.childCount - 1)
            //Set ScrollView at the end
            scrollView.post {
                scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }

        DoneButton.setOnClickListener {

            val userId = getSharedPreferences("Login", Context.MODE_PRIVATE)
                .getString("userId", "errorid")

            var members = mutableListOf<String>()
            var me: String = ""

            for (i in 0..InputVerticalList.childCount) {
                try {
                    if (InputVerticalList.getChildAt(i).MemberNameInput.text.toString() != "") {
                        members.add(InputVerticalList.getChildAt(i).MemberNameInput.text.toString().trim())
                    }

                    if (InputVerticalList.getChildAt(i).ItsMeInput.isChecked) {
                        me = InputVerticalList.getChildAt(i).MemberNameInput.text.toString()
                    }
                } catch (e: Exception) {}
            }

            var array = members.toTypedArray()

            if (me != "") {
                val hashMap = HashMap<String, Any>()
                hashMap.put("title", WalletNameInput.text.toString())
                hashMap.put("owner", userId)
                hashMap.put("members", array)
                hashMap.put("description", DescriptionInput.text.toString())
                hashMap.put("me", me)

                viewModel.sendData(hashMap)
                    .doOnNext {

                        if (it == "Wallet created") {
                            val intent = Intent(this@CreateWalletActivity, HomeActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        } else {
                            this.runOnUiThread {
                                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    .doOnError { it.printStackTrace() }
                    .doOnComplete { println("onComplete!") }
                    .subscribe()
            } else {
                Toast.makeText(this, "me is empty", Toast.LENGTH_LONG).show()
            }
        }
    }
}
