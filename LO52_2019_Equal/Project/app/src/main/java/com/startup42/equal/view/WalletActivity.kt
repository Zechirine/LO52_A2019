package com.startup42.equal.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.startup42.equal.Equal
import com.startup42.equal.R
import com.startup42.equal.model.WalletResult
import com.startup42.equal.viewModel.ShareViewModel
import com.startup42.equal.viewModel.WalletViewModel
import kotlinx.android.synthetic.main.activity_wallet.*
import kotlinx.android.synthetic.main.popup_share.*
import kotlinx.android.synthetic.main.popup_share.view.*


class WalletActivity : AppCompatActivity(), HistoryFragment.OnFragmentInteractionListener,
    MoreFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
    }

    var walletId : String = ""
    var userId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        walletId = intent.getStringExtra("walletId")
        userId = getSharedPreferences("Login", Context.MODE_PRIVATE)
            .getString("userId", "errorid")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)



        var tabLayout = tab
        var viewPager = view

        var pageAdapter = PageAdapter(supportFragmentManager, tabLayout.tabCount, walletId, userId)
        viewPager.adapter = pageAdapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.setCurrentItem(tab.getPosition())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

       shareButton.setOnClickListener {
            // Create an instance of the dialog fragment and show it
            val dialog = ShareDialogFragment.newInstance(walletId)
            dialog.show(supportFragmentManager, "ShareDialogFragment")
        }
    }
}

private const val ARG_PARAM1 = "param1"

class ShareDialogFragment : DialogFragment() {

    val viewModel = WalletViewModel()
    private var walletId: String = ""
    var wallets: WalletResult? = null
    var walletNameText : String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let {
            walletId = it.getString(ARG_PARAM1)
        }



        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            var v : View = inflater.inflate(R.layout.popup_share, null)

            var name : TextView = v.findViewById(R.id.walletNameSharePopup)

            viewModel.receiveData(walletId)
                .doOnNext {
                    wallets = it
                    it.title?.let{
                        activity?.runOnUiThread {
                            name.text = it
                        }
                    }
                }
                .doOnError { it.printStackTrace() }
                .doOnComplete{ println("onComplete!") }
                .subscribe()

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.Share,
                    DialogInterface.OnClickListener { dialog, id ->

                            var shareViewModel = ShareViewModel()
                            val hashMap = HashMap<String, Any>()

                            var emailInput : TextView = v.findViewById(R.id.emailShareDialog)
                            var memberNameInput : TextView = v.findViewById(R.id.memberNameShareDialog)

                            hashMap.put("walletId", walletId)
                            hashMap.put("email", emailInput.text.toString())
                            hashMap.put("memberName", memberNameInput.text.toString())

                            shareViewModel.sendData(hashMap).doOnNext {

                                if (it == "The wallet was correctly shared") {

                                    //Toast.makeText(getActivity(), "The wallet was correctly shared", Toast.LENGTH_LONG).show()

                                } else {
                                    //Toast.makeText(getActivity(), it, Toast.LENGTH_LONG).show()
                                }
                            }
                                .doOnError { it.printStackTrace() }
                                .doOnComplete{ println("onComplete!") }
                                .subscribe()

                    })
                .setNegativeButton(R.string.Cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog().cancel()
                    })
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")



    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MoreFragment.
         */
        @JvmStatic
        fun newInstance(walletId : String) =
            ShareDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, walletId)
                }
            }
    }

}


class PageAdapter internal constructor(fm: FragmentManager, private val numOfTabs: Int, walletId : String, userId : String) :
    FragmentPagerAdapter(fm) {

    var walletId = walletId
    var userId = userId

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                val historyFrag = HistoryFragment.newInstance(walletId, userId)
                return historyFrag}

            1 -> {
                val moreFrag = MoreFragment.newInstance(walletId, userId)
                return moreFrag
            }

            else -> return null
        }
    }

    override fun getCount(): Int {
        return numOfTabs
    }
}