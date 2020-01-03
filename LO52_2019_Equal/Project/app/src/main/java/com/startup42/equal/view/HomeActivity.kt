package com.startup42.equal.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.startup42.equal.R
import com.startup42.equal.model.HomeResult
import com.startup42.equal.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class HomeActivity : AppCompatActivity() {

    private val viewModel = HomeViewModel()
    private var wallets: ArrayList<HomeResult>? = null

    private var adapter: HomeAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userId = getSharedPreferences("Login", Context.MODE_PRIVATE)
            .getString("userId", "errorid")

        setSupportActionBar(customToolbar)
        setupToolBar()

        viewModel.receiveData(userId)
            .doOnNext {
                wallets = it
                this.runOnUiThread{
                    adapter =  HomeAdapter(this,it)
                    walletsListView.adapter = adapter

                }
            }
            .doOnError { it.printStackTrace() }
            .doOnComplete{ println("onComplete!") }
            .subscribe()

        addWalletButton.setOnClickListener{
            val intent = Intent(this, CreateWalletActivity::class.java)
            startActivity(intent)
        }

        walletsListView.setOnItemClickListener { _, _, position, _ ->
            /** TODO Redirect to flux view */
            val intent = Intent(this, CreateFluxActivity::class.java)
            val wallet = wallets?.get(position)
            //intent.putExtra("wallet",wallet)
            startActivity(intent)
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.receiveData(userId)
                .doOnNext {
                    this@HomeActivity.runOnUiThread {
                        this@HomeActivity.adapter?.updateWallets(it)
                        this@HomeActivity.adapter?.notifyDataSetChanged()
                    }
                }
                .doOnError { it.printStackTrace() }
                .doOnComplete{ println("onComplete!") }
                .subscribe()

            val handler = Handler()
            handler.postDelayed({
                if (swipeRefresh.isRefreshing) {
                    swipeRefresh.isRefreshing = false
                }
            }, 1000)
        }
    }

    private fun setupToolBar(){
        backButton.visibility = View.INVISIBLE

        titleTextView.setTextColor(getResources().getColor(R.color.textColorOnMainColor))
        titleTextView.text = getSharedPreferences("Login", Context.MODE_PRIVATE)
            .getString("userTag", "userTagError")
    }

    private class HomeAdapter(context: Context, wallets: ArrayList<HomeResult>): BaseAdapter() {

        private val mContext: Context = context
        private var mWallets: ArrayList<HomeResult> = wallets

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val row = layoutInflater.inflate(R.layout.row_home_wallets, parent, false)

            return setupCell(row,mWallets[position])
        }

        override fun getItem(position: Int): Any {
            return "Test STRING"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return mWallets.size
        }

        fun updateWallets(wallets: ArrayList<HomeResult>){
            mWallets = wallets
        }

        private fun setupCell(row: View, wallet: HomeResult): View {
            val walletNameTextView = row.findViewById<TextView>(R.id.walletName)
            walletNameTextView.text = wallet.title

            var membersString = ""
            if (wallet.members > 0) {
                membersString += wallet.members.toString() + " members"
            }

            if (wallet.shared > 0) {
                membersString += " - " + wallet.shared.toString() + " shares"
            }

            val walletMemberTextView = row.findViewById<TextView>(R.id.memberWallet)
            walletMemberTextView.text = membersString

            val userBalanceTextView = row.findViewById<TextView>(R.id.userBalance)

            var text = ""
            if (wallet.userBalance > 0) {
                userBalanceTextView.setTextColor(mContext.getResources().getColor(R.color.darkGreen))
                text = "+"
            } else if ( wallet.userBalance < 0) {
                userBalanceTextView.setTextColor(mContext.getResources().getColor(R.color.red))
            } else {
                userBalanceTextView.setTextColor(mContext.getResources().getColor(R.color.black))
            }

            userBalanceTextView.text = text + wallet.userBalance.toString() + "€"

            return row
        }
    }
}
