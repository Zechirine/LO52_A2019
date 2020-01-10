package com.example.lo52_f1_levier.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lo52_f1_levier.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_participant.*

class ParticipantFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_participant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Force the display of the first Tab
        var fragment : Fragment
        fragment = ParticipantTabAjouterFragment().apply {}
        childFragmentManager
            .beginTransaction()
            .replace(R.id.toReplaceWithFragment, fragment)
            .commit()

        // listener on the TabLayout
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                // Tab Add
                if(tab!!.position == 0){
                    fragment = ParticipantTabAjouterFragment().apply {}
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.toReplaceWithFragment, fragment)
                        .commit()
                }
                // Tab Consult...
                if(tab!!.position == 1){
                    fragment = ParticipantTabConsultFragment().apply {}
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.toReplaceWithFragment, fragment)
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    companion object {
    }
}
