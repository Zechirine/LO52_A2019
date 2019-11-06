package com.startup42.equal.view

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.startup42.equal.R
import com.startup42.equal.model.HomeResult
import com.startup42.equal.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    val viewModel = HomeViewModel()
    var wallets: ArrayList<HomeResult>? = null

    private var adapter: HomeAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userId = getSharedPreferences("Login", Context.MODE_PRIVATE)
            .getString("userId", "errorid")

        val listView = findViewById<ListView>(R.id.wallets)
        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.customToolbar)
        setSupportActionBar(toolbar)
        setupToolBar()

        viewModel.receiveData(userId)
            .doOnNext {
                wallets = it
                this.runOnUiThread{
                    adapter =  HomeAdapter(this,it)
                    listView.adapter = adapter

                }
            }
            .doOnError { it.printStackTrace() }
            .doOnComplete{ println("onComplete!") }
            .subscribe()

        addWalletButton.setOnClickListener{
/* TODO redirect to the add wallet activity
            val intent = Intent(this, ::class.java)
            startActivity(intent)
*/
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
            handler.postDelayed(object:Runnable {
                override fun run() {
                    if (swipeRefresh.isRefreshing()) {
                        swipeRefresh.setRefreshing(false)
                    }
                }
            }, 1000)
        }
    }

    private fun setupToolBar(){
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.visibility = View.INVISIBLE

        val usertag = findViewById<TextView>(R.id.titleTextView)
        usertag.setTextColor(getResources().getColor(R.color.textColorOnMainColor))
        usertag.text = getSharedPreferences("Login", Context.MODE_PRIVATE)
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
