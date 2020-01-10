package com.example.fgurlsdev.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fgurlsdev.Adapters.MyExpandableListAdapter;
import com.example.fgurlsdev.DaoUtils.CommonDaoUtils;
import com.example.fgurlsdev.DaoUtils.DaoUtilsCollection;
import com.example.fgurlsdev.Entity.Group;
import com.example.fgurlsdev.Entity.PassagePart;
import com.example.fgurlsdev.Entity.PassagePartDao;
import com.example.fgurlsdev.Entity.Runner;
import com.example.fgurlsdev.Entity.RunnerDao;
import com.example.fgurlsdev.R;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;


public class RunnerFragment extends Fragment {
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";

    private static final String TAG = "RunnerFragment";

    private Button btnAdd;
    private Button btnOk;

    private TableLayout mTableLayout;
    private EditText searchRunner;
    private List<Runner> runners = new ArrayList<>();
    private List<Runner> modifyList;

    private CommonDaoUtils<Runner> runnerDaoUtils;
    private CommonDaoUtils<Group> groupDaoUtils;

    // Rename and change types of parameters
    //private String mParam1;

    public RunnerFragment() {
        // Required empty public constructor
    }

    public static RunnerFragment newInstance() {
        RunnerFragment fragment = new RunnerFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public List<Runner> getRunners() {
        return runners;
    }

    // Verify if name already exists in database or not
    private boolean nameExists(String name) {
        for (Runner runner : runners) {
            if (runner.getNom().equals(name)) {
                Toast.makeText(getContext(), name + " existe deja !\nAjoutez un chiffre a la fin.", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return  false;
    }

    private void appendNewRow() {
        final TableRow tableRow = new TableRow(getActivity());

        final TextView nomPrenom = new TextView(getActivity());
        nomPrenom.setText("");

        final EditText setRunnerName = new EditText(getActivity());
        setRunnerName.setText("");
        setRunnerName.setHint(R.string.hint_enter_runner);
        setRunnerName.setGravity(Gravity.CENTER);

        final Button annulRunner = new Button(getActivity());
        annulRunner.setText(R.string.btn_annul);
        annulRunner.setGravity(Gravity.CENTER);
        annulRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTableLayout.removeView(tableRow);
            }
        });

        final Button confirmRunner = new Button(getActivity());
        confirmRunner.setText(R.string.btn_confirm);
        confirmRunner.setGravity(Gravity.CENTER);
        confirmRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = setRunnerName.getText().toString();

                if(msg.equals("")){
                    Toast.makeText(getActivity(), "Information manquante.", Toast.LENGTH_LONG).show();
                    return;
                }

                Runner currentRunner;
                List<Runner> dbRunner = runnerDaoUtils.getByQueryBuilder(RunnerDao.Properties.Nom.eq(msg));
                if (dbRunner != null && dbRunner.size() == 1){
                    Toast.makeText(getContext(), msg + " existe deja !\nAjoutez un chiffre a la fin.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    currentRunner = new Runner(null, msg, false);
                    runnerDaoUtils.insertEntity(currentRunner);
                    List<Runner> dbInsertRunner = runnerDaoUtils.getByQueryBuilder(RunnerDao.Properties.Nom.eq(msg));
                    runners.add(dbInsertRunner.get(0));
                    setDBTable(dbInsertRunner.get(0));
                }

                // Remove modify view
                tableRow.removeAllViews();
                mTableLayout.removeView(tableRow);
            }
        });

        tableRow.addView(setRunnerName);
        tableRow.addView(annulRunner);
        tableRow.addView(confirmRunner);

        mTableLayout.addView(tableRow);
    }

    private void setDBTable(final Runner runner){
        final TableRow tr = new TableRow(getActivity());

            final TextView nomPrenom = new TextView(getActivity());
            nomPrenom.setText(runner.getNom());

            final Button delRunner = new Button(getActivity());
            delRunner.setText(R.string.btn_delete);
            delRunner.setGravity(Gravity.CENTER);
            delRunner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Verify if runner has a group
                    if (runner.getHasGroup()) {
                        new AlertDialog.Builder(getContext())
                                //.setIcon(R.drawable.ic_launcher)
                                .setTitle("Verification de suppression")
                                .setMessage(runner.getNom() + " est actuellement dans une equipe, voulez-vous le supprimer quand meme ?\n(Si oui, il sera enleve de cette equipe)")
                                .setNegativeButton("Annul",null)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    private boolean groupInCourse(String groupName){
                                        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
                                        CommonDaoUtils<PassagePart> psgPartDaoUtils = daoUtilsCollection.getPassagePartDaoUtils();
                                        if (PassagePartDao.Properties.GroupName.eq(groupName) == null) return false;
                                        if (PassagePartDao.Properties.PartResult.isNull() == null) return false;
                                        List<PassagePart> passageParts = psgPartDaoUtils.getByQueryBuilder(PassagePartDao.Properties.GroupName.eq(groupName),
                                                PassagePartDao.Properties.PartResult.isNull(),
                                                new WhereCondition.StringCondition("1 GROUP BY competitionId"));
                                        if (passageParts == null || passageParts.size() <= 0) return false;
                                        new AlertDialog.Builder(getContext())
                                                .setTitle("Notification")
                                                .setMessage(runner.getNom() + " est actuellement dans une course, vous ne pouvez pas le supprimer.")
                                                .setNeutralButton("Ok", null).show();
                                        return true;
                                    }

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        List<Group> groups = groupDaoUtils.getAllEntities();
                                        for (Group group : groups) {
                                            if (group.getRunnerId1().equals(runner.getRunnerId())) {
                                                if (groupInCourse(group.getGroupName())) return;
                                                group.setRunnerId1(null);
                                                group.setNbMembers(group.getNbMembers() - 1);
                                                groupDaoUtils.updateEntity(group);
                                                break;
                                            }
                                            if (group.getRunnerId2().equals(runner.getRunnerId())) {
                                                if (groupInCourse(group.getGroupName())) return;
                                                group.setRunnerId2(null);
                                                group.setNbMembers(group.getNbMembers() - 1);
                                                groupDaoUtils.updateEntity(group);
                                                break;
                                            }
                                            if (group.getRunnerId3().equals(runner.getRunnerId())) {
                                                if (groupInCourse(group.getGroupName())) return;
                                                group.setRunnerId3(null);
                                                group.setNbMembers(group.getNbMembers() - 1);
                                                groupDaoUtils.updateEntity(group);
                                                break;
                                            }
                                        }
                                        // Remove runner from BDD
                                        runnerDaoUtils.deleteEntity(runner);
                                        mTableLayout.removeView(tr);
                                        // Refresh data in ExpandableListView
                                        ExpandableListView mListView = getActivity().findViewById(R.id.groupView);
                                        if (mListView != null) {
                                            MyExpandableListAdapter mListAdapter = new MyExpandableListAdapter<Group>(getContext(), mListView, Group.class, Runner.class);
                                            mListView.setAdapter(mListAdapter);
                                        }
                                    }
                                }).show();
                    } else {
                        runnerDaoUtils.deleteEntity(runner);
                        mTableLayout.removeView(tr);
                    }
                }
            });

            final Button modifyRunner = new Button(getActivity());
            modifyRunner.setText(R.string.btn_modify);
            modifyRunner.setGravity(Gravity.CENTER);
            modifyRunner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tr.removeAllViews();

                    String etContent = nomPrenom.getText().toString();
                    final EditText setRunnerName = new EditText(getActivity());
                    setRunnerName.setText(etContent);
                    setRunnerName.setSelectAllOnFocus(true);

                    Button confirmRunner = new Button(getActivity());
                    confirmRunner.setText(R.string.btn_confirm);
                    confirmRunner.setGravity(Gravity.CENTER);
                    confirmRunner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tr.removeAllViews();
                            String confirmName = setRunnerName.getText().toString();

                            // Verify if name modified exists or not in database
                            if (!confirmName.equals(runner.getNom()) && !nameExists(confirmName)){
                                nomPrenom.setText(confirmName);

                                // Update in database
                                runner.setNom(confirmName);
                                runnerDaoUtils.updateEntity(runner);
                            }

                            tr.addView(nomPrenom);
                            tr.addView(modifyRunner);
                            tr.addView(delRunner);
                        }
                    });

                    tr.addView(setRunnerName);
                    tr.addView(confirmRunner);
                }
            });

            tr.addView(nomPrenom);
            tr.addView(modifyRunner);
            tr.addView(delRunner);
            mTableLayout.addView(tr);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
        /*if (savedInstanceState != null) {}*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_runner, container, false);
        // attachToRoot : Whether the inflated hierarchy should be attached to the root parameter?
        // If false, root is only used to create the correct subclass of LayoutParams for the root view in the XML.
        mTableLayout = mView.findViewById(R.id.coureurLayout);
        btnAdd = mView.findViewById(R.id.btnAddRunner);
        //searchRunner = mView.findViewById(R.id.searchRunnerName);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendNewRow();
            }
        });

        //Charge data from BDD
        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
        runnerDaoUtils = daoUtilsCollection.getRunnerDaoUtils();
        groupDaoUtils = daoUtilsCollection.getGroupDaoUtils();
        // Initialize data
        runners = runnerDaoUtils.getAllEntities();

        // Add table in the view
        for (Runner runner : runners) {
            setDBTable(runner);
        }

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        /*FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commitAllowingStateLoss();*/
        super.onSaveInstanceState(outState);
        //String str = "";
        //outState.putString("", str);
    }
}
