package com.example.fgurlsdev.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.example.fgurlsdev.Adapters.MyExpandableListAdapter;
import com.example.fgurlsdev.OtherActivities.CreateCompetition;
import com.example.fgurlsdev.DaoUtils.CommonDaoUtils;
import com.example.fgurlsdev.DaoUtils.DaoUtilsCollection;
import com.example.fgurlsdev.Entity.Competition;
import com.example.fgurlsdev.Entity.Group;
import com.example.fgurlsdev.Entity.PassagePart;
import com.example.fgurlsdev.R;

import java.util.HashMap;
import java.util.List;


public class CompetitionFragment extends Fragment {
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "Course";

    private ExpandableListView mListView;
    private Button addCompetition;

    private MyExpandableListAdapter mListAdapter;

    private CommonDaoUtils<PassagePart> passagePartDaoUtils;
    private CommonDaoUtils<Competition> competitionDaoUtils;
    private CommonDaoUtils<Group> groupDaoUtils;

    // Rename and change types of parameters
    //private String mParam1;

    public CompetitionFragment() {
        // Required empty public constructor
    }

    public static CompetitionFragment newInstance() {
        CompetitionFragment fragment = new CompetitionFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_competition, container, false);

        mListView = mView.findViewById(R.id.competitionView);
        mListView.setGroupIndicator(null);

        addCompetition = mView.findViewById(R.id.btnAddCompetition);

        addCompetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump to CreateCompetition activity
                Intent intent = new Intent(getActivity(), CreateCompetition.class);
                startActivity(intent);
            }
        });

        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
        passagePartDaoUtils = daoUtilsCollection.getPassagePartDaoUtils();
        competitionDaoUtils = daoUtilsCollection.getCompetitionDaoUtils();
        groupDaoUtils = daoUtilsCollection.getGroupDaoUtils();

        // Set list view
        mListAdapter = new MyExpandableListAdapter<Competition>(getContext(), mListView, Competition.class, Group.class);
        mListView.setAdapter(mListAdapter);

        return mView;
    }
}