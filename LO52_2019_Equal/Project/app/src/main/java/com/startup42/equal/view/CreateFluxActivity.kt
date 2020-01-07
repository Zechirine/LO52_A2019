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

    private var type: String = "expense"
    private var isDone = false
    val viewModel = CreateFluxViewModel()
    var amount: Int? = null
    var members = ArrayList<MembersCreateFlux>()

    private fun setupToolBar() {

        titleTextView.setTextColor(this.resources.getColor(R.color.textColorOnMainColor))
        titleTextView.text = getString(R.string.newFlux)

        backButton.setColorFilter(
            ContextCompat.getColor(this, R.color.textColorOnMainColor),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )

        backButton.setOnClickListener{
            finish()
        }
    }

    private fun setupView(memberList: ArrayList<String>){
        setSupportActionBar(customToolbar)
        setupToolBar()

        segmentedButton.setTintColor(getResources().getColor(R.color.mainColor))
        segmentedButton.check(R.id.expenseSB)

        val sec1 = SectionParicipation(memberList,SectionParameters.builder()
            .itemResourceId(R.layout.row_createflux)
            .headerResourceId(R.layout.header_createflux)
            .build())

        val sec2 = SectionInvolvement(memberList,SectionParameters.builder()
            .itemResourceId(R.layout.row_createflux)
            .headerResourceId(R.layout.header_createflux)
            .build())

        val sectionAdapter = SectionedRecyclerViewAdapter()
        sectionAdapter.addSection(sec1)
        sectionAdapter.addSection(sec2)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = sectionAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_flux)

        val memberList = intent.getStringArrayListExtra("memberList")
        val walletId = intent.getStringExtra("walletID")

        setupView(memberList)

        for (member in memberList) {
            members.add(MembersCreateFlux(member,0.0,0.0))
        }

        amountTextField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
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

                segmentedButton.setOnCheckedChangeListener { _, checkedId ->
                    when(checkedId) {
                        R.id.expenseSB -> type = "expense"
                        R.id.incomeSB -> type = "income"
                    }
                }

                val hashMap = HashMap<String, Any>()
                hashMap["title"] = fluxNameTextField.text.toString()
                hashMap["walletId"] = walletId
                hashMap["type"] = type
                hashMap["amount"] = amountTextField.text.toString().toDouble()
                hashMap["members"] = membersToSend
                hashMap["userId"] = userId

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
        val dictMember: ArrayList<HashMap<String,Any>> = ArrayList()
        for (memberLine in 0..(members.size-1)) {
            val hashmap = HashMap<String, Any>()
            hashmap["name"] = members[memberLine].name
            hashmap["from"] = members[memberLine].from
            hashmap["to"] = members[memberLine].to
            dictMember.add(hashmap)
        }
        return dictMember
    }

    private class HeaderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val header:TextView = itemView.findViewById(R.id.headerName)
    }

    inner class SectionParicipation(val nameMembers: ArrayList<String>,
                                    sectionParameters: SectionParameters ): Section(sectionParameters) {

        override fun getContentItemsTotal(): Int {
            return nameMembers.size
        }

        override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            val myItemViewHolder = holder as ViewHolderParticipation
            myItemViewHolder.bind(nameMembers.get(position),position)
        }

        override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
            view?.setOnClickListener {
                val checkBox = view.findViewById<CheckBox>(R.id.isConcerned)
                checkBox.isChecked = !checkBox.isChecked
            }
            return ViewHolderParticipation(view!!)
        }

        override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
            val headerViewHolder = holder as HeaderViewHolder
            headerViewHolder.header.text = getString(R.string.headerParticipation)
        }

        override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
            return HeaderViewHolder(view!!)
        }
    }

    inner class ViewHolderParticipation(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(name: String,position: Int) {
            setupCell(itemView,name)

            val amountTextField = itemView.findViewById<EditText>(R.id.amountRowTextField)
            amountTextField.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    if(amount != null) {
                        val checkBox = itemView.findViewById<CheckBox>(R.id.isConcerned)
                        if (s.isNotEmpty()) {
                            members.get(position).from = s.toString().toDouble()
                            checkBox.isChecked = true
                        } else {
                            checkBox.isChecked = false
                        }
                    } else {
                        Toast.makeText(
                            this@CreateFluxActivity,
                            getString(R.string.errorAmount),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

            val percentageTextField = itemView.findViewById<EditText>(R.id.percentageTextField)
            percentageTextField.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    if (s.isNotEmpty()) {
                        if (amount != null) {
                            val percentageDouble = s.toString().toDouble()
                            if (percentageDouble < 100.0) {
                                val amountWithPercentage = amount?.times((percentageDouble / 100))
                                val amountTextField =
                                    itemView.findViewById<EditText>(R.id.amountRowTextField)
                                amountTextField.setText(amountWithPercentage.toString())
                            } else {
                                Toast.makeText(
                                    this@CreateFluxActivity,
                                    getString(R.string.errorPercentage),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@CreateFluxActivity,
                                getString(R.string.errorAmount),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
        }

        private fun setupCell(row: View, memberName: String): View {
            val memberNameTextView = row.findViewById<TextView>(R.id.nameMember)
            memberNameTextView.text = memberName

            return row
        }
    }

    inner class SectionInvolvement(val nameMembers: ArrayList<String>,
                                   sectionParameters: SectionParameters ): Section(sectionParameters) {

        override fun getContentItemsTotal(): Int {
            return nameMembers.size
        }

        override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            val myItemViewHolder = holder as ViewHolderInvolvement
            myItemViewHolder.bind(nameMembers.get(position),position)
        }

        override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
            view?.setOnClickListener {
                val checkBox = view.findViewById<CheckBox>(R.id.isConcerned)
                checkBox.isChecked = !checkBox.isChecked
            }
            return ViewHolderInvolvement(view!!)
        }

        override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
            val headerViewHolder = holder as HeaderViewHolder
            headerViewHolder.header.text = getString(R.string.headerInvolvements)
        }

        override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
            return HeaderViewHolder(view!!)
        }
    }

    inner class ViewHolderInvolvement(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(name: String,position: Int) {
            setupCell(itemView,name)

            val amountTextField = itemView.findViewById<EditText>(R.id.amountRowTextField)
            amountTextField.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    if (amount != null) {
                        val checkBox = itemView.findViewById<CheckBox>(R.id.isConcerned)
                        if (s.isNotEmpty()) {
                            members.get(position).to = s.toString().toDouble()
                            checkBox.isChecked = true
                        } else {
                            checkBox.isChecked = false
                        }
                    } else {
                        Toast.makeText(
                            this@CreateFluxActivity,
                            getString(R.string.errorAmount),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

            val percentageTextField = itemView.findViewById<EditText>(R.id.percentageTextField)
            percentageTextField.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    if (s.isNotEmpty()){
                        if (amount != null){
                            val percentageDouble = s.toString().toDouble()
                            if (percentageDouble < 100.0) {
                                val amountWithPercentage = amount?.times((percentageDouble / 100))
                                val amountTextField =
                                    itemView.findViewById<EditText>(R.id.amountRowTextField)
                                amountTextField.setText(amountWithPercentage.toString())
                            } else {
                                Toast.makeText(this@CreateFluxActivity,getString(R.string.errorPercentage),Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@CreateFluxActivity,getString(R.string.errorAmount),Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        }

        private fun setupCell(row: View, memberName: String): View {
            val memberNameTextView = row.findViewById<TextView>(R.id.nameMember)
            memberNameTextView.text = memberName
            return row
        }
    }
}
