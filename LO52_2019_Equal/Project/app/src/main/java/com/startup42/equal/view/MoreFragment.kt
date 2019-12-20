package com.startup42.equal.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.startup42.equal.Equal
import com.startup42.equal.R
import com.startup42.equal.model.WalletBalance
import com.startup42.equal.model.WalletResult
import com.startup42.equal.viewModel.WalletViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MoreFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreFragment : Fragment() {
    val viewModel = WalletViewModel()

    private var walletId: String = ""
    private var userId: String = ""
    private var adapter: FluxAdapter? = null
    var wallets: WalletResult? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            walletId = it.getString(ARG_PARAM1)
            userId = it.getString(ARG_PARAM2)
        }

        println("TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEST")

        println(walletId)
        println(userId)

        viewModel.receiveData(walletId)
            .doOnNext {
                wallets = it
                activity?.runOnUiThread{
                    it.balances?.let{

                        activity?.runOnUiThread {

                            println("ADAPTEEEEEEEEEEEEEEEEEEEEER")

                            adapter = FluxAdapter(
                                Equal.context,
                                it
                            )

                            var listView = activity?.findViewById<ListView>(R.id.list)
                            if(listView == null)
                                println("NUUUUUUUUUUUUUUUUUUUUUUUULL")

                            listView?.adapter = adapter
                        }
                    }
                }


            }
            .doOnError { it.printStackTrace() }
            .doOnComplete{ println("onComplete!") }
            .subscribe()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
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
        fun newInstance(walletId : String, userId : String) =
            MoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, walletId)
                    putString(ARG_PARAM2, userId)
                }
            }
    }

    private class FluxAdapter(context: Context, balance: ArrayList<WalletBalance>): BaseAdapter() {

        private val mContext: Context = context
        private var mBalance: ArrayList<WalletBalance> = balance

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val row = layoutInflater.inflate(R.layout.row_wallet_balance_item, parent, false)

            return setupCell(row,mBalance[position])
        }

        override fun getItem(position: Int): Any {
            return "Test STRING"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return mBalance.size
        }

        fun updateWallets(balance: ArrayList<WalletBalance>){
            mBalance = balance
        }

        private fun setupCell(row: View, balance: WalletBalance): View {

            println(balance.from)

            val balanceFrom = row.findViewById<TextView>(R.id.balanceFrom)
            balanceFrom.text = balance.from

            val balanceTo = row.findViewById<TextView>(R.id.balanceTo)
            balanceTo.text = balance.to

            val balanceAmount = row.findViewById<TextView>(R.id.BalanceAmount)
            balanceAmount.text = balance.amount.toString()+"€"

            return row
        }


    }
}
