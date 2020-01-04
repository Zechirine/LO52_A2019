package com.startup42.equal.view

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.startup42.equal.R
import com.startup42.equal.viewModel.CreateFluxViewModel
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

import kotlinx.android.synthetic.main.activity_create_flux.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class CreateFluxActivity : AppCompatActivity() {

    class MembersCreateFlux(val name: String, var from: Double, var to: Double)

    val viewModel = CreateFluxViewModel()
    var amount: Int? = null
    var type: String = "expense"
    var members = ArrayList<MembersCreateFlux>()

    var isDone = false

    private fun setupToolBar() {

        titleTextView.setTextColor(getResources().getColor(R.color.textColorOnMainColor))
        titleTextView.text = "New Flux"

        backButton.setColorFilter(
            ContextCompat.getColor(this, R.color.textColorOnMainColor),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )

        backButton.setOnClickListener{
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_flux)

        setSupportActionBar(customToolbar)
        setupToolBar()

        segmentedButton.setTintColor(getResources().getColor(R.color.mainColor))
        segmentedButton.check(R.id.expenseSB)

        //val memberList = intent.getStringArrayListExtra("memberList")
        val walletId = intent.getStringExtra("walletID")

        val memberList = ArrayList<String>()
        memberList.add("Alban")
        memberList.add("Anthony")
        memberList.add("Thomas")

        for (member in memberList) {
            members.add(MembersCreateFlux(member,0.0,0.0))
        }

        val sec1 = SectionParicipation(memberList,SectionParameters.builder()
            .itemResourceId(R.layout.row_createflux)
            .headerResourceId(R.layout.header_createflux)
            .build())

        val sec2 = SectionParicipation(memberList,SectionParameters.builder()
            .itemResourceId(R.layout.row_createflux)
            .headerResourceId(R.layout.header_createflux)
            .build())


        val sectionAdapter = SectionedRecyclerViewAdapter()
        sectionAdapter.addSection(sec1)
        sectionAdapter.addSection(sec2)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = sectionAdapter



        amountTextField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    //TODO cells
                    amount = s.toString().toIntOrNull()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })

        doneButtonFlux.setOnClickListener {

            if(!isDone) {
                isDone = true

                val userId = getSharedPreferences("Login", Context.MODE_PRIVATE)
                    .getString("userId", "errorid")

                val membersToSend = membersToSend()

                segmentedButton.setOnCheckedChangeListener { group, checkedId ->
                    when(checkedId) {
                        R.id.expenseSB -> type = "expense"
                        R.id.incomeSB -> type = "income"
                    }
                }

                val hashMap = HashMap<String, Any>()
                hashMap.put("title", fluxNameTextField.text.toString())
                hashMap.put("walletId",walletId)
                hashMap.put("type",type)
                hashMap.put("amount", amountTextField.text.toString())
                hashMap.put("members", membersToSend)
                hashMap.put("userId", userId)


                viewModel.sendData(hashMap)
                    .doOnNext {
                        if (it == "Flux created") {
                            finish()
                        } else {
                            this.runOnUiThread {
                                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                                isDone = false
                            }
                        }
                    }
                    .doOnError { it.printStackTrace() ; isDone = false}
                    .doOnComplete { println("onComplete!") }
                    .subscribe()
            }
        }
    }

    private fun membersToSend(): ArrayList<HashMap<String,Any>> {
    var dictMember: ArrayList<HashMap<String,Any>> = ArrayList()
        for (memberLine in 0..(members.size-1)) {
            val hashmap = HashMap<String, Any>()
            hashmap.put("name",members[memberLine].name)
            hashmap.put("from",members[memberLine].from)
            hashmap.put("to",members[memberLine].to)
            dictMember.add(hashmap)
        }
        return dictMember
    }

    private class SectionParicipation(val nameMembers: ArrayList<String>,
                                      sectionParameters: SectionParameters ): Section(sectionParameters) {

        override fun getContentItemsTotal(): Int {
            return nameMembers.size
        }

        override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            val myItemViewHodler = holder as MyItemViewHodler
            myItemViewHodler.bind(nameMembers.get(position))
        }

        override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
            view?.setOnClickListener { view
                val checkBox = view.findViewById<CheckBox>(R.id.isConcerned)
                checkBox.isChecked = !checkBox.isChecked
            }

            val amount = view?.findViewById<EditText>(R.id.amountRowTextField)
            // TODO listener to amount and percentage
            return MyItemViewHodler(view!!)
        }
    }

    private class MyItemViewHodler(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(name: String) {
            setupCell(itemView,name)
        }

        private fun setupCell(row: View, memberName: String): View {
            val memberNameTextView = row.findViewById<TextView>(R.id.nameMember)
            memberNameTextView.text = memberName
/* TODO cells

            val amountTextField = row.findViewById<EditText>(R.id.amountRowTextField)
            amountTextField.isEnabled = false
            amountTextField.isFocusable = false

            val percentageTextField = row.findViewById<EditText>(R.id.percentageTextField)
            percentageTextField.isEnabled = false
            percentageTextField.isFocusable = false*/

            return row
        }
    }
}
