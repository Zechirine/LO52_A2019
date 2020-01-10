package com.example.fgurlsdev.OtherActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.GridLayout;

import com.example.fgurlsdev.DaoUtils.CommonDaoUtils;
import com.example.fgurlsdev.DaoUtils.DaoUtilsCollection;
import com.example.fgurlsdev.Entity.Competition;
import com.example.fgurlsdev.Entity.Group;
import com.example.fgurlsdev.Entity.GroupDao;
import com.example.fgurlsdev.Entity.Passage;
import com.example.fgurlsdev.Entity.PassageDao;
import com.example.fgurlsdev.Entity.PassagePart;
import com.example.fgurlsdev.Entity.PassagePartDao;
import com.example.fgurlsdev.Entity.Runner;
import com.example.fgurlsdev.MainActivity;
import com.example.fgurlsdev.R;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LaunchGameActivity extends AppCompatActivity {
    //button click time
    int runner; //runner id in group.
    int numGroup;
    GridLayout listGroup;
    private Chronometer chronoAll;
    private Button buttonResult;
    private String TAG = "gameActivity";
    private CommonDaoUtils<Competition> gameDaoUtils;
    private CommonDaoUtils<Passage> turnDaoUtils;
    private static CommonDaoUtils<PassagePart> timerDaoUtils;
    private CommonDaoUtils<Group> groupDaoUtils;
    private CommonDaoUtils<Runner> runnerDaoUtils;
    private Long competitionId;
    private List<Group> groups = new ArrayList<>();
    private HashMap<Group, Integer> counter = new HashMap<>();
    private HashMap<Group, List<PassagePart>> partResult = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_game);
        //Button to start one match
        final Button buttonBegin = findViewById(R.id.btBegin);
        listGroup = findViewById(R.id.listGroup);
        //Timer of whole match
        chronoAll = findViewById(R.id.chrnoAll);
        //Click to view the result
        buttonResult = findViewById(R.id.btResult);

        // Get competition to launch
        competitionId = getIntent().getLongExtra("competitionId", -1);

        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
        gameDaoUtils = daoUtilsCollection.getCompetitionDaoUtils();
        turnDaoUtils = daoUtilsCollection.getPassageDaoUtils();
        timerDaoUtils = daoUtilsCollection.getPassagePartDaoUtils();
        groupDaoUtils = daoUtilsCollection.getGroupDaoUtils();
        runnerDaoUtils = daoUtilsCollection.getRunnerDaoUtils();

        // Get all participated groups
        Competition game = gameDaoUtils.getEntityById(competitionId);
        List<PassagePart> psgPartsGroupByGroupName = timerDaoUtils.getByQueryBuilder(PassagePartDao.Properties.CompetitionId.eq(competitionId),
                new WhereCondition.StringCondition("1 GROUP BY groupName"));
        List<Group> tmpGroups = new ArrayList<>();
        for (PassagePart psgPart : psgPartsGroupByGroupName) {
            Group group = groupDaoUtils.getUniqueByQuery(GroupDao.Properties.GroupName.eq(psgPart.getGroupName()));
            tmpGroups.add(group);
        }

        //gameId = game.getGameId();
        groups = tmpGroups;
        numGroup = groups.size();
        //Log.i(TAG, groups.toString());

        //groups.size();
        for (Group group : groups) {
            View oneGroup = View.inflate(this, R.layout.content_timer, null);
            Button buttonGroup = oneGroup.findViewById(R.id.btGroup);
            buttonGroup.setText(group.getGroupName());
            listGroup.addView(oneGroup);
            counter.put(group, 0);
            partResult.put(group, new ArrayList<PassagePart>());
        }

        //Start the match
        buttonBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronoAll.setBase(SystemClock.elapsedRealtime());
                chronoAll.start();
                for (int i = 0; i < numGroup; i++) {
                    Button buttonGroup = listGroup.getChildAt(i).findViewById(R.id.btGroup);
                    buttonGroup.setEnabled(true);
                    Chronometer chronoGroup = listGroup.getChildAt(i).findViewById(R.id.chrnoGroup);
                    buttonGroup.setBackgroundColor(getResources().getColor(R.color.color4));
                    buttonGroup.setText(R.string.label_sprint1);
                    chronoGroup.setBase(SystemClock.elapsedRealtime());
                    chronoGroup.start();
                }
            }
        });

        Log.i("testTime1", Long.toString(System.currentTimeMillis()));

        // final Long turnId = turnPerRunner.getTurnId();
        for (int i = 0; i < groups.size(); i++) {
            final int numberGroup = i;
            final Button buttonGroup = listGroup.getChildAt(i).findViewById(R.id.btGroup);
            final Chronometer chronoGroup = listGroup.getChildAt(i).findViewById(R.id.chrnoGroup);
            final Group g = groups.get(i);

            buttonGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int round = counter.get(g) % 5;
                    int runnerTurn = counter.get(g) / 5;
                    int ctn = counter.get(g) + 1;
                    counter.put(g, ctn);

                    switch (round) {
                        case 0:
                            buttonGroup.setBackgroundColor(getResources().getColor(R.color.color0));
                            buttonGroup.setText(R.string.label_tourObstacle1);
                            // Set result of Passage(round)-Sprint 1
                            long elapsedMillis = SystemClock.elapsedRealtime() - chronoGroup.getBase();
                            chronoGroup.setBase(SystemClock.elapsedRealtime());
                            setOneTimer(runnerTurn, g, "Sprint 1", elapsedMillis);
                            break;
                        case 1:
                            buttonGroup.setBackgroundColor(getResources().getColor(R.color.color1));
                            buttonGroup.setText(R.string.label_pitStop);
                            // Set result of Passage(round)-Tour D'obstacle 1
                            long elapsedMillis2 = SystemClock.elapsedRealtime() - chronoGroup.getBase();
                            chronoGroup.setBase(SystemClock.elapsedRealtime());
                            setOneTimer(runnerTurn, g, "Tour D\'obstacle 1", elapsedMillis2);
                            break;
                        case 2:
                            buttonGroup.setBackgroundColor(getResources().getColor(R.color.color2));
                            buttonGroup.setText(R.string.label_sprint2);
                            // Set result of Passage(round)-Pit-Stop
                            long elapsedMillis3 = SystemClock.elapsedRealtime() - chronoGroup.getBase();
                            chronoGroup.setBase(SystemClock.elapsedRealtime());
                            setOneTimer(runnerTurn, g, "Pit-Stop", elapsedMillis3);
                            break;
                        case 3:
                            buttonGroup.setBackgroundColor(getResources().getColor(R.color.color3));
                            buttonGroup.setText(R.string.label_tourObstacle2);
                            // Set result of Passage(round)-Sprint 2
                            long elapsedMillis4 = SystemClock.elapsedRealtime() - chronoGroup.getBase();
                            chronoGroup.setBase(SystemClock.elapsedRealtime());
                            setOneTimer(runnerTurn, g, "Sprint 2", elapsedMillis4);
                            break;
                        case 4:
                            buttonGroup.setBackgroundColor(getResources().getColor(R.color.color4));
                            long elapsedMillis5 = SystemClock.elapsedRealtime() - chronoGroup.getBase();
                            // Set result of Passage(round)-Tour D'obstacle 2
                            chronoGroup.setBase(SystemClock.elapsedRealtime());
                            setOneTimer(runnerTurn, g, "Tour D\'obstacle 2", elapsedMillis5);
                            runnerDone(runnerTurn, numberGroup);

                            if (counter.get(g) == 30){
                                chronoGroup.setVisibility(View.INVISIBLE);
                                buttonGroup.setText("End");
                                buttonGroup.setEnabled(false);
                                if(isEnd()) endGame();
                            } else {
                                buttonGroup.setText(R.string.label_sprint1);
                            }
                            break;
                    }
                }
            });
        }
        //Log.i("testTime3", Long.toString(System.currentTimeMillis()));

        buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchGameActivity.this, MainActivity.class);
                intent.putExtra("fragmentId", 4);
                startActivity(intent);
            }
        });
    }

    public boolean isEnd(){
        for(Group group : groups) {
            if(!counter.get(group).equals(30)) return false;
        }
        return true;
    }

    public void endGame() {
        chronoAll.stop();
        for (Group g : groups) {
            List<PassagePart> psgParts = partResult.get(g);
            for (PassagePart psgPart : psgParts) {
                List<PassagePart> dbPsgPart = timerDaoUtils.getByQueryBuilder(PassagePartDao.Properties.CompetitionId.eq(competitionId),
                        PassagePartDao.Properties.PsgOrder.eq(psgPart.getPsgOrder()),
                        PassagePartDao.Properties.PartLabel.eq(psgPart.getPartLabel()),
                        PassagePartDao.Properties.GroupName.eq(g.getGroupName()),
                        PassagePartDao.Properties.RunnerName.eq(psgPart.getRunnerName())
                );
                PassagePart tmpPsgPart = dbPsgPart.get(0);
                tmpPsgPart.setPartResult(psgPart.getPartResult());
                timerDaoUtils.updateEntity(tmpPsgPart);
            }
        }
    }

    public void runnerDone(int count, int group) {
        switch (count) {
            case 0:
                runner = R.id.runner11;
                break;
            case 1:
                runner = R.id.runner12;
                break;
            case 2:
                runner = R.id.runner13;
                break;
            case 3:
                runner = R.id.runner21;
                break;
            case 4:
                runner = R.id.runner22;
                break;
            case 5:
                runner = R.id.runner23;
                break;
        }
        CheckBox check = listGroup.getChildAt(group).findViewById(runner);
        if (check != null) check.setVisibility(View.VISIBLE);
    }

    public void setOneTimer(int runnerTurn, Group group, String label, Long result) {
        List<PassagePart> passagePartList = partResult.get(group);
        if(passagePartList == null) return;

        int passageOrder = 1;
        Runner runner = new Runner();
        switch (runnerTurn) {
            case 0:
                // First runner, first turn
                runner = runnerDaoUtils.getEntityById(group.getRunnerId1());
                passageOrder = 1;
                break;
            case 1:
                // Second runner, first turn
                runner = runnerDaoUtils.getEntityById(group.getRunnerId2());
                passageOrder = 1;
                break;
            case 2:
                // Third runner, first turn
                runner = runnerDaoUtils.getEntityById(group.getRunnerId3());
                passageOrder = 1;
                break;
            case 3:
                // First runner, second turn
                runner = runnerDaoUtils.getEntityById(group.getRunnerId1());
                passageOrder = 2;
                break;
            case 4:
                // Second runner, second turn
                runner = runnerDaoUtils.getEntityById(group.getRunnerId2());
                passageOrder = 2;
                break;
            case 5:
                // Third runner, second turn
                runner = runnerDaoUtils.getEntityById(group.getRunnerId3());
                passageOrder = 2;
                break;
        }

        if(runner.getRunnerId() != null) {
            passagePartList.add(new PassagePart(null, label,runner.getNom(), null, result, null, passageOrder));
            partResult.put(group, passagePartList);
        }
    }
}
