package com.example.lo52_f1_levier.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.lo52_f1_levier.DAO.CoureurDao
import com.example.lo52_f1_levier.R
import kotlinx.android.synthetic.main.activity_paticipant_edit.*
import kotlinx.android.synthetic.main.fragment_participant_tab_ajouter.*
import kotlinx.android.synthetic.main.fragment_participant_tab_ajouter.btn_addParticipant
import kotlinx.android.synthetic.main.fragment_participant_tab_ajouter.btn_cancel
import kotlinx.android.synthetic.main.fragment_participant_tab_ajouter.firstName
import kotlinx.android.synthetic.main.fragment_participant_tab_ajouter.lastName
import kotlinx.android.synthetic.main.fragment_participant_tab_consult.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ParticipantTabAjouterFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ParticipantTabAjouterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParticipantTabAjouterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participant_tab_ajouter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        btn_addParticipant.setOnClickListener{
            if(firstName.text.toString() != "" && lastName.text.toString() != ""){
                val coureurDao = CoureurDao(this.context!!)
                coureurDao.insertCoureur(firstName.text.toString(),lastName.text.toString())
                firstName.text.clear()
                lastName.text.clear()
            }
            else{
                val builder = AlertDialog.Builder(this.context!!)
                builder.setTitle("Attention")
                builder.setMessage("Veuillez bien remplir toutes les informations nécessaires.")
                builder.show()

            }
        }
        btn_cancel.setOnClickListener {
            firstName.text.clear()
            lastName.text.clear()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParticipantTabAjouterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParticipantTabAjouterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
