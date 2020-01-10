package com.example.fgurlsdev.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TableLayout;

import com.example.fgurlsdev.Adapters.MyExpandableListAdapter;
import com.example.fgurlsdev.OtherActivities.CreateGroup;
import com.example.fgurlsdev.DaoUtils.CommonDaoUtils;
import com.example.fgurlsdev.DaoUtils.DaoUtilsCollection;
import com.example.fgurlsdev.Entity.Group;
import com.example.fgurlsdev.Entity.Runner;
import com.example.fgurlsdev.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GroupFragment extends Fragment {
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "Equipe";

    // Rename and change types of parameters
    //private String mParam1;

    private Button btnAdd;
    //private Button btnOk;

    private ExpandableListView mListView;
    private MyExpandableListAdapter mListAdapter;

    private CommonDaoUtils<Group> groupDaoUtils;
    private CommonDaoUtils<Runner> runnerDaoUtils;
    private List<Group> groups = new ArrayList<>();
    private HashMap<Group, List<Object>> listHashMap = new HashMap<>();

    public GroupFragment() {
        // Required empty public constructor
    }

    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
        for(int i = 0 ; i < listHashMap.size() ; i++)
            mListView.collapseGroup(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_group, container, false);
        mListView = mView.findViewById(R.id.groupView);
        mListView.setGroupIndicator(null);

        btnAdd = mView.findViewById(R.id.btnAddGroup);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump to CreateGroup activity
                Intent intent = new Intent(getActivity(), CreateGroup.class);
                startActivity(intent);
            }
        });
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Initialize data
        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
        groupDaoUtils = daoUtilsCollection.getGroupDaoUtils();
        runnerDaoUtils = daoUtilsCollection.getRunnerDaoUtils();

        // Set list view adapter
        mListAdapter = new MyExpandableListAdapter<Group>(getContext(), mListView, Group.class, Runner.class);
        mListView.setAdapter(mListAdapter);
    }
}