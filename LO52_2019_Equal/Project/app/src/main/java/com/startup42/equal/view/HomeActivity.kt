package com.startup42.equal.view

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.startup42.equal.R
import com.startup42.equal.model.HomeResult
import com.startup42.equal.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    val viewModel = HomeViewModel()
    var wallets: ArrayList<HomeResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val prefs = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val userId = prefs.getString("userId", "errorid")

        val listView = findViewById<ListView>(R.id.wallets)


        viewModel.receiveData(userId)
            .doOnNext {
                wallets = it
                this.runOnUiThread{
                    listView.adapter = HomeAdapter(this,it)
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
    }


    private class HomeAdapter(context: Context, wallets: ArrayList<HomeResult>): BaseAdapter() {

        private val mContext: Context = context
        private val mWallets: ArrayList<HomeResult> = wallets

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
