package com.example.fgurlsdev.Fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fgurlsdev.DaoUtils.CommonDaoUtils;
import com.example.fgurlsdev.DaoUtils.DaoUtilsCollection;
import com.example.fgurlsdev.Entity.Competition;
import com.example.fgurlsdev.Entity.CompetitionDao;
import com.example.fgurlsdev.Entity.Group;
import com.example.fgurlsdev.Entity.Passage;
import com.example.fgurlsdev.Entity.PassageDao;
import com.example.fgurlsdev.Entity.PassagePart;
import com.example.fgurlsdev.Entity.PassagePartDao;
import com.example.fgurlsdev.R;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class StatisticsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "Param1";

    // Rename and change types of parameters
    //private String mParam1;

    private View mView;

    private CommonDaoUtils<Competition> competitionDaoUtils;
    private CommonDaoUtils<Passage> passageDaoUtils;
    private CommonDaoUtils<PassagePart> passagePartDaoUtils;
    private CommonDaoUtils<Group> groupDaoUtils;

    private String[] params = {"Runner", " LIMIT 1", "", "", ""};

    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
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
        mView = inflater.inflate(R.layout.fragment_statistics, container, false);
        final RadioGroup chGroupRunner = mView.findViewById(R.id.chGroupRunner);
        final RadioButton radioBtnRunner = mView.findViewById(R.id.radioBtnRunner);

        final RadioGroup chTop = mView.findViewById(R.id.chTop);
        final RadioButton radioBtnTop1 = mView.findViewById(R.id.radioBtnTop1);

        final Spinner chCompetition = mView.findViewById(R.id.chCompetition);
        final Spinner chPassage = mView.findViewById(R.id.chPassage);
        final Spinner chPassagePart = mView.findViewById(R.id.chPassagePart);

        ArrayAdapter<String> competitionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        ArrayAdapter<String> passageAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        ArrayAdapter<String> psgPartAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);

        // Set Dao utils
        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
        competitionDaoUtils = daoUtilsCollection.getCompetitionDaoUtils();
        passageDaoUtils = daoUtilsCollection.getPassageDaoUtils();
        passagePartDaoUtils = daoUtilsCollection.getPassagePartDaoUtils();
        groupDaoUtils = daoUtilsCollection.getGroupDaoUtils();

        // Set radioGroup OnCheckedChangeListener
        radioBtnRunner.setChecked(true);
        chGroupRunner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBtnRunner) params[0] = "Runner";
                else params[0] = "Group";
                setData();
            }
        });

        radioBtnTop1.setChecked(true);
        chTop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtnTop3:
                        params[1] = " LIMIT 3";
                        break;
                    case R.id.radioBtnAll:
                        params[1] = "";
                        break;
                    default:
                        params[1] = " LIMIT 1";
                        break;
                }
                setData();
            }
        });

        // Set spinner chose competition
        List<Competition> competitions = competitionDaoUtils.getByQueryBuilder(CompetitionDao.Properties.CompetitionDate.isNotNull());
        competitionAdapter.add("");
        for (Competition competition : competitions)
            competitionAdapter.add(competition.getCompetitionName());
        competitionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chCompetition.setAdapter(competitionAdapter);
        chCompetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = chCompetition.getSelectedItem().toString();
                params[2] = choice;
                setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set spinner chose passage
        passageAdapter.addAll("", "1", "2");
        passageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chPassage.setAdapter(passageAdapter);
        chPassage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = chPassage.getSelectedItem().toString();
                params[3] = choice;
                // Set Data
                setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set spinner chose passage part
        psgPartAdapter.addAll("", "Sprint 1", "Tour D\'obstacle 1", "Pit-Stop", "Sprint 2", "Tour D\'obstacle 2");
        psgPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chPassagePart.setAdapter(psgPartAdapter);
        chPassagePart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = chPassagePart.getSelectedItem().toString();
                params[4] = choice;
                // Set Data
                setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return mView;
    }

    private void setData() {
        // Reset view : tableLayout
        TableLayout tableLayout = mView.findViewById(R.id.resultData);
        tableLayout.removeAllViews();

        TableRow titleTableRow = new TableRow(getActivity());

        TextView titleRunnerName = new TextView(getActivity());
        titleRunnerName.setText(R.string.label_simple_runner);
        TextView titleGroupName = new TextView(getActivity());
        titleGroupName.setText(R.string.label_simple_groupName);
        TextView titleCourse = new TextView(getActivity());
        titleCourse.setText(R.string.label_simple_competition);
        TextView titlePassage = new TextView(getActivity());
        titlePassage.setText(R.string.label_simple_passage);
        TextView titlePsgPart = new TextView(getActivity());
        titlePsgPart.setText(R.string.label_simple_turn);
        TextView titleResult = new TextView(getActivity());
        titleResult.setText(R.string.label_simple_result);

        titleTableRow.addView(titleCourse);
        if (!params[3].equals("")) titleTableRow.addView(titlePassage);
        if (!params[4].equals("")) titleTableRow.addView(titlePsgPart);
        if (params[0].equals("Runner")) titleTableRow.addView(titleRunnerName);
        titleTableRow.addView(titleGroupName);
        titleTableRow.addView(titleResult);

        tableLayout.addView(titleTableRow);

        List<PassagePart> showPsgParts = new ArrayList<>();
        List<Competition> competitionList = competitionDaoUtils.getByQueryBuilder(CompetitionDao.Properties.CompetitionDate.isNotNull());
        String conditionLine = "1";
        String endCondition = "";
        List<Competition> dbCompetition = new ArrayList<>();

        // If chose passage's order
        if (!params[3].equals("")) {
            conditionLine = conditionLine + " AND psgOrder = " + params[3];
        }
        // If chose passagePart's name
        if (!params[4].equals("")) {
            conditionLine = conditionLine + " AND partLabel = \"" + params[4] + "\"";
        }

        // If chose competition name
        if (!params[2].equals("")) {
            dbCompetition = competitionDaoUtils.getByQueryBuilder(CompetitionDao.Properties.CompetitionName.eq(params[2]));
            conditionLine = conditionLine + " AND competitionId = " + dbCompetition.get(0).getCompetitionId();
            //endCondition = " ORDER BY partResult ASC" + params[1];
            showPsgParts.addAll(getShowContent(conditionLine));
        } else {
            for (Competition comp : competitionList) {
                String tmpConditionLine = conditionLine + " AND competitionId = " + comp.getCompetitionId();
                showPsgParts.addAll(getShowContent(tmpConditionLine));
            }
        }

        // Set data view
        for (PassagePart psgPart : showPsgParts) {
            TableRow dataTableRow = new TableRow(getActivity());

            TextView dataRunnerName = new TextView(getActivity());
            TextView dataGroupName = new TextView(getActivity());
            TextView dataCourse = new TextView(getActivity());
            TextView dataPassage = new TextView(getActivity());
            TextView dataPsgPart = new TextView(getActivity());
            TextView dataResult = new TextView(getActivity());

            dataCourse.setText(competitionDaoUtils.getEntityById(psgPart.getCompetitionId()).getCompetitionName());
            String psgContent = "" + psgPart.getPsgOrder();
            dataPassage.setText(psgContent);
            dataPsgPart.setText(psgPart.getPartLabel());
            dataRunnerName.setText(psgPart.getRunnerName());
            dataGroupName.setText(psgPart.getGroupName());
            String resultContent = (psgPart.getPartResult().doubleValue() / 1000) + " s";
            dataResult.setText(resultContent);

            dataTableRow.removeAllViews();
            dataTableRow.addView(dataCourse);
            if (!params[3].equals("")) dataTableRow.addView(dataPassage);
            if (!params[4].equals("")) dataTableRow.addView(dataPsgPart);
            if (params[0].equals("Runner")) dataTableRow.addView(dataRunnerName);
            dataTableRow.addView(dataGroupName);
            dataTableRow.addView(dataResult);

            tableLayout.addView(dataTableRow);
        }
    }

    // Get show content data
    private List<PassagePart> getShowContent(String conditionLine){
        List<PassagePart> result = new ArrayList<>();
        String getPartsCondition = "";
        String setResultCondition = "";
        if (params[0].equals("Group")) getPartsCondition = " GROUP BY groupName";
        else getPartsCondition = " GROUP BY runnerName";

        List<PassagePart> psgParts = passagePartDaoUtils.getByQueryBuilder(new WhereCondition.StringCondition(conditionLine + getPartsCondition));
        for (PassagePart psgPart : psgParts) {

            if (params[0].equals("Group")) setResultCondition = " AND groupName = \"" + psgPart.getGroupName() + "\"  GROUP BY groupName";
            else setResultCondition = " AND runnerName = \"" + psgPart.getRunnerName() + "\"  GROUP BY runnerName";

            psgPart.setPartResult(passagePartDaoUtils.getSumByNativeSql("partResult", "PASSAGE_PART",
                    conditionLine + setResultCondition));
        }
        if (psgParts.size() > 1) {
            // Sort passagePart with partResult
            Collections.sort(psgParts, new Comparator<PassagePart>() {
                @Override
                public int compare(PassagePart o1, PassagePart o2) {
                    if (o1.getPartResult().doubleValue() > o2.getPartResult().doubleValue())
                        return 1;
                    return -1;
                }
            });
        }
        if (params[1].equals(" LIMIT 1")) {
            result.add(psgParts.get(0));
        } else if (params[1].equals(" LIMIT 3") && psgParts.size() > 3) {
            result.add(psgParts.get(0));
            result.add(psgParts.get(1));
            result.add(psgParts.get(2));
        } else result = psgParts;
        return result;
    }
}
