package com.startup42.equal.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.startup42.equal.model.WalletFlux
import com.startup42.equal.model.WalletResult
import com.startup42.equal.viewModel.WalletViewModel
import com.daimajia.swipe.SwipeLayout
import com.startup42.equal.Equal
import com.startup42.equal.R
import com.startup42.equal.viewModel.ApproveFluxViewModel
import com.startup42.equal.viewModel.DeclineFluxViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HistoryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    val viewModel = WalletViewModel()

    private var walletId: String = ""
    private var userId: String = ""
    private var adapter: WalletAdapter? = null
    var wallets: WalletResult? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            walletId = it.getString(ARG_PARAM1)
            userId = it.getString(ARG_PARAM2)
        }



        viewModel.receiveData(walletId)
            .doOnNext {
                wallets = it
                activity?.runOnUiThread{
                    it.flux?.let{

                        activity?.runOnUiThread {

                            adapter = WalletAdapter(
                                Equal.context,
                                wallets?.flux,
                                userId
                            )

                            var listView = activity?.findViewById<RecyclerView>(R.id.historySwipeLayout)
                            listView?.setLayoutManager(LinearLayoutManager(Equal.context))

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

        val view: View = inflater!!.inflate(R.layout.fragment_history, container, false)

        view.addFlux.setOnClickListener{

            println("CLICKED")

            val intent = Intent(getActivity(), CreateFluxActivity::class.java)
            intent.putExtra("walletID",walletId)
            val list = arrayListOf<String>()

            wallets?.members?.let{
                for (member in wallets?.members!!) {
                    list.add(member.name)
                }
            }

            intent.putStringArrayListExtra("memberList",list)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return view
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
         * @return A new instance of fragment HistoryFragment.
         */
        @JvmStatic
        fun newInstance(walletId : String, userId : String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, walletId)
                    putString(ARG_PARAM2, userId)
                }
            }
    }

    private class WalletAdapter() : RecyclerSwipeAdapter<WalletAdapter.SimpleViewHolder>() {

        val approveViewModel = ApproveFluxViewModel()
        val declineViewModel = DeclineFluxViewModel()

        private var fluxList: ArrayList<WalletFlux>? = null
        private var mContext: Context? = null

        private var mUserid: String? = null

        constructor(context: Context, objects: ArrayList<WalletFlux>?, userid : String) : this() {
            this.mContext = context
            this.fluxList = objects

            this.mUserid = userid
        }

        override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {

            var flux : WalletFlux? = fluxList?.get(position)

            viewHolder.swipeLayout?.setShowMode(SwipeLayout.ShowMode.PullOut)

            //dari kiri
            viewHolder.swipeLayout?.addDrag(
                SwipeLayout.DragEdge.Left,
                viewHolder.swipeLayout?.findViewById(R.id.FluxLayout)
            )

            //dari kanan
            viewHolder.swipeLayout?.addDrag(
                SwipeLayout.DragEdge.Right,
                viewHolder.swipeLayout?.findViewById(R.id.FluxLayout)
            )


            flux?.let{

                var title : String = ""
                flux.from?.let {
                    title = flux.title
                }
                val walletFluxTitle = viewHolder.fluxName
                walletFluxTitle?.text = title

                var amount : Double = 0.0
                flux.amount?.let {
                    amount = flux.amount
                }
                val walletAmount = viewHolder.pendingAmount
                walletAmount?.text = amount.toString() + "€"


                var membersToString = ""
                flux.to?.let {
                    for( member in flux.to!!){

                        membersToString += " - " + member.name

                    }
                }

                val walletMemberToTextView = viewHolder.memberListTo
                walletMemberToTextView?.text = membersToString

                var membersFromString = ""
                flux.from?.let {
                    for( member in flux.from!!){

                        membersFromString += member.name + " "

                    }
                }

                val walletMemberTextView = viewHolder.memberList
                walletMemberTextView?.text = membersFromString


                val status = viewHolder.fluxStatus
                //approved/declined/pending
                when (flux.status) {
                    "approved" -> status?.setImageResource(R.drawable.validated_flux)
                    "declined" -> status?.setImageResource(R.drawable.declined_flux)
                    "pending" -> status?.setImageResource(R.drawable.pending_flux)
                    else -> status?.setImageResource(R.drawable.back_arrow)
                }

                val approvedButton = viewHolder.approvedButton
                val declinedButton = viewHolder.declinedButton

                approvedButton?.setOnClickListener{

                    val hashMap = HashMap<String, Any>()
                    hashMap.put("userId", mUserid.toString())
                    hashMap.put("fluxId", flux.fluxId)

                    approveViewModel.sendData(hashMap)
                        .doOnNext {

                            if (it == "You just approved the flux") {

                                println("You just approved the flux")


                            } else {
                                println(it)
                            }
                        }
                        .doOnError { it.printStackTrace() }
                        .doOnComplete{ println("onComplete!") }
                        .subscribe()
                }

                declinedButton?.setOnClickListener{

                    val hashMap = HashMap<String, Any>()
                    hashMap.put("userId", mUserid.toString())
                    hashMap.put("fluxId", flux.fluxId)

                    declineViewModel.sendData(hashMap)
                        .doOnNext {

                            if (it == "You just declined the flux") {

                                println("You just declined the flux")


                            } else {
                                println(it)
                            }
                        }
                        .doOnError { it.printStackTrace() }
                        .doOnComplete{ println("onComplete!") }
                        .subscribe()
                }

                when (flux.status) {
                    "approved" -> {approvedButton?.visibility = View.GONE
                        declinedButton?.visibility = View.GONE
                    }
                    "declined" -> {approvedButton?.visibility = View.GONE
                        declinedButton?.visibility = View.GONE
                    }
                    "pending" -> {
                        approvedButton?.visibility = View.VISIBLE
                        declinedButton?.visibility = View.VISIBLE
                    }
                    else -> {approvedButton?.visibility = View.GONE
                        declinedButton?.visibility = View.GONE
                    }
                }
            }

            viewHolder.swipeLayout?.addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onStartOpen(layout: SwipeLayout) {

                }

                override fun onOpen(layout: SwipeLayout) {

                }

                override fun onStartClose(layout: SwipeLayout) {

                }

                override fun onClose(layout: SwipeLayout) {

                }

                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {

                }

                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {

                }
            })

            mItemManger.bindView(viewHolder.itemView, position)

        }

        override fun getItemCount(): Int {

            fluxList.let{
                return fluxList!!.size
            }

            return 0

        }

        override fun getSwipeLayoutResourceId(position: Int): Int {
            return R.id.FluxLayout
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SimpleViewHolder {

            val view : View =LayoutInflater.from(Equal.context).inflate(
                R.layout.row_wallet_item, parent, false)
            return SimpleViewHolder(view)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var swipeLayout: SwipeLayout? = null
            var fluxName:TextView? = null
            var fluxStatus:ImageView? = null
            var pendingAmount:TextView? = null
            var memberListTo:TextView? = null
            var memberList:TextView? = null
            var approvedButton : TextView? = null
            var declinedButton : TextView? = null

            init {
                swipeLayout = itemView.findViewById(R.id.historySwipeLayout)
                fluxName = itemView.findViewById(R.id.fluxName)
                fluxStatus = itemView.findViewById(R.id.fluxStatus)
                pendingAmount = itemView.findViewById(R.id.pendingAmount)
                memberListTo = itemView.findViewById(R.id.memberListTo)
                memberList = itemView.findViewById(R.id.memberList)
                approvedButton = itemView.findViewById(R.id.approvedButton)
                declinedButton = itemView.findViewById(R.id.declinedButton)
            }
        }


    }
}
