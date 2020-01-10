package com.example.fgurlsdev.OtherActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fgurlsdev.Adapters.MyAutoCompleteTextAdapter;
import com.example.fgurlsdev.DaoUtils.CommonDaoUtils;
import com.example.fgurlsdev.DaoUtils.DaoUtilsCollection;
import com.example.fgurlsdev.Entity.Competition;
import com.example.fgurlsdev.Entity.CompetitionDao;
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
import java.util.List;

public class CreateCompetition extends AppCompatActivity {
    private TableLayout tableLayout;
    private EditText newCompetitionName;
    private ImageView addGroupIcon;
    private Button confirmCompetition;
    private Button annulCompetition;

    private Competition currentCompetition;

    private CommonDaoUtils<Competition> competitionDaoUtils;
    private CommonDaoUtils<Passage> passageDaoUtils;
    private CommonDaoUtils<PassagePart> passagePartDaoUtils;
    private CommonDaoUtils<Group> groupDaoUtils;
    private CommonDaoUtils<Runner> runnerDaoUtils;

    private String mode = "Create";

    private void setContent(long competitionId) {
        currentCompetition = competitionDaoUtils.getEntityById(competitionId);
        newCompetitionName.setText(currentCompetition.getCompetitionName());

        final List<PassagePart> psgParts = passagePartDaoUtils.getByQueryBuilder(
                PassagePartDao.Properties.CompetitionId.eq(currentCompetition.getCompetitionId()),
                new WhereCondition.StringCondition("1 GROUP BY groupName"));

        for (final PassagePart psgPart : psgParts) {
            final TableRow tableRow = new TableRow(this);

            final TextView labelGroup = new TextView(this);
            labelGroup.setText(R.string.label_group);
            labelGroup.setGravity(Gravity.CENTER);

            final AutoCompleteTextView addGroupName = new AutoCompleteTextView(this);
            addGroupName.setText(psgPart.getGroupName());
            addGroupName.setHint(R.string.hint_enter_group);
            addGroupName.setGravity(Gravity.START);
            addGroupName.setThreshold(1);
            setAutoCompleteText(addGroupName);

            final Button removeGroup = new Button(this);
            removeGroup.setText(R.string.btn_delete);
            removeGroup.setGravity(Gravity.CENTER);
            removeGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove from view
                    tableLayout.removeView(tableRow);
                    // Remove from database
                    List<PassagePart> removePsgParts = passagePartDaoUtils.getByQueryBuilder(
                            PassagePartDao.Properties.CompetitionId.eq(psgPart.getCompetitionId()),
                            PassagePartDao.Properties.GroupName.eq(psgPart.getGroupName()));
                    passagePartDaoUtils.deleteEntities(removePsgParts);
                    // Update nbGroups
                    currentCompetition.setNbGroups(currentCompetition.getNbGroups() - 1);
                }
            });

            tableRow.addView(labelGroup);
            tableRow.addView(addGroupName);
            tableRow.addView(removeGroup);
            tableLayout.addView(tableRow);
        }
    }

    private void setAutoCompleteText(AutoCompleteTextView autoCompleteText) {
        List<Group> groups = groupDaoUtils.getByQueryBuilder(GroupDao.Properties.RunnerId1.isNotNull(), GroupDao.Properties.RunnerId2.isNotNull(), GroupDao.Properties.RunnerId3.isNotNull());
        MyAutoCompleteTextAdapter groupSpinnerAdapter = new MyAutoCompleteTextAdapter<>(this, Group.class, groups);
        autoCompleteText.setAdapter(groupSpinnerAdapter);
    }

    private void appendNewRaw() {
        final TableRow tableRow = new TableRow(this);

        final TextView groupName = new TextView(this);
        groupName.setText(R.string.label_group);
        groupName.setGravity(Gravity.CENTER);

        final AutoCompleteTextView addGroupName = new AutoCompleteTextView(this);
        addGroupName.setText("");
        addGroupName.setHint(R.string.hint_enter_group);
        addGroupName.setGravity(Gravity.START);
        addGroupName.setThreshold(1);
        setAutoCompleteText(addGroupName);

        final Button removeGroup = new Button(this);
        removeGroup.setText(R.string.btn_delete);
        removeGroup.setGravity(Gravity.CENTER);
        removeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableLayout.removeView(tableRow);
            }
        });

        tableRow.addView(groupName);
        tableRow.addView(addGroupName);
        tableRow.addView(removeGroup);
        tableLayout.addView(tableRow);
    }

    public List<String> getEditTextsContents() {
        int childCount = tableLayout.getChildCount();
        List<String> groupsNames = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            EditText editText = (EditText) tableRow.getChildAt(1);
            groupsNames.add(editText.getText().toString());
        }
        return groupsNames;
    }

    public boolean competitionNameExists(String newCompetitionName) {
        if (competitionDaoUtils.getByQueryBuilder(CompetitionDao.Properties.CompetitionName.eq(newCompetitionName)).size() > 0) {
            Toast.makeText(getApplicationContext(), "Nom de course existe deja!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private Competition addNewCompetition(String newCompetitionName) {
        Competition newCompetition = new Competition(null, newCompetitionName, 0, null);
        competitionDaoUtils.insertEntity(newCompetition);
        List<Competition> dbCompetitions = competitionDaoUtils.getByQueryBuilder(CompetitionDao.Properties.CompetitionName.eq(newCompetitionName));
        if (dbCompetitions != null && dbCompetitions.size() == 1) {
            newCompetition = dbCompetitions.get(0);
            return newCompetition;
        }
        return null;
    }

    private List<Passage> addPassages() {
        passageDaoUtils.insertEntity(new Passage(null, "Passage 1", 1));
        passageDaoUtils.insertEntity(new Passage(null, "Passage 2", 2));
        List<Passage> dbPassages = passageDaoUtils.getAllEntities();
        if (dbPassages != null && dbPassages.size() == 2) return dbPassages;
        return null;
    }

    private void addPassageParts(long competitionId, int psgOrder) {
        List<String> groupsNames = getEditTextsContents();
        for (String groupName : groupsNames) {
            List<Group> dbGroup = groupDaoUtils.getByQueryBuilder(GroupDao.Properties.GroupName.eq(groupName));
            Group group = dbGroup.get(0);

            List<Runner> runners = new ArrayList<>();
            runners.add(runnerDaoUtils.getEntityById(group.getRunnerId1()));
            runners.add(runnerDaoUtils.getEntityById(group.getRunnerId2()));
            runners.add(runnerDaoUtils.getEntityById(group.getRunnerId3()));

            for (Runner runner : runners) {
                PassagePart passagePart;
                passagePart = new PassagePart(null, "Sprint 1", runner.getNom(), groupName, null, competitionId, psgOrder);
                passagePartDaoUtils.insertEntity(passagePart);
                passagePart = new PassagePart(null, "Tour D\'obstacle 1", runner.getNom(), groupName, null, competitionId, psgOrder);
                passagePartDaoUtils.insertEntity(passagePart);
                passagePart = new PassagePart(null, "Pit-Stop", runner.getNom(), groupName, null, competitionId, psgOrder);
                passagePartDaoUtils.insertEntity(passagePart);
                passagePart = new PassagePart(null, "Sprint 2", runner.getNom(), groupName, null, competitionId, psgOrder);
                passagePartDaoUtils.insertEntity(passagePart);
                passagePart = new PassagePart(null, "Tour D\'obstacle 2", runner.getNom(), groupName, null, competitionId, psgOrder);
                passagePartDaoUtils.insertEntity(passagePart);
            }
        }
    }

    private boolean groupNameIsDuplicate() {
        List<String> groupsNames = getEditTextsContents();
        if (groupsNames.size() > 0) {
            for (int i = 0; i < groupsNames.size(); i++) {
                for (int j = 0; j < groupsNames.size(); j++) {
                    if (i != j && groupsNames.get(i).equals(groupsNames.get(j))) {
                        Toast.makeText(this, groupsNames.get(i) + " est duplique !", Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean groupIsReady(){
        if(groupNameIsDuplicate()) return false;
        List<String> groupsNames = getEditTextsContents();
        for (String groupName : groupsNames) {
            List<Group> dbGroup = groupDaoUtils.getByQueryBuilder(GroupDao.Properties.GroupName.eq(groupName));
            // If no such group name
            if ((dbGroup == null || dbGroup.size() < 1)) {
                Toast.makeText(getApplicationContext(), groupName + " n\'existe pas !", Toast.LENGTH_LONG).show();
                return false;
            }
            Group group = dbGroup.get(0);
            // Verify if group has enough runners
            if (group.getRunnerId1() == null || group.getRunnerId2() == null || group.getRunnerId3() == null) {
                Toast.makeText(getApplicationContext(), groupName + " n\'a pas assez de coureurs !", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    // Jump to competition fragment
    private void jumpToCompetition() {
        Intent intent = new Intent(CreateCompetition.this, MainActivity.class);
        intent.putExtra("fragmentId", 3);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_competition);

        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
        competitionDaoUtils = daoUtilsCollection.getCompetitionDaoUtils();
        passageDaoUtils = daoUtilsCollection.getPassageDaoUtils();
        passagePartDaoUtils = daoUtilsCollection.getPassagePartDaoUtils();
        groupDaoUtils = daoUtilsCollection.getGroupDaoUtils();
        runnerDaoUtils = daoUtilsCollection.getRunnerDaoUtils();

        tableLayout = findViewById(R.id.addGroups);
        newCompetitionName = findViewById(R.id.newCompetitionName);
        addGroupIcon = findViewById(R.id.addGroupToCompetitionIcon);
        confirmCompetition = findViewById(R.id.confirmAddCompetition);
        annulCompetition = findViewById(R.id.annulAddCompetition);

        // If require to modify competition
        long competitionId = getIntent().getLongExtra("competitionId", -1);
        if (competitionId != -1) {
            setContent(competitionId);
            mode = "Modify";
        } else mode = "Create";

        addGroupIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendNewRaw();
            }
        });

        confirmCompetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = newCompetitionName.getText().toString();
                if (newName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Le nom de course est vide !", Toast.LENGTH_LONG).show();
                    return;
                }

                // Verify if groups are ready to be added
                if(!groupIsReady()) return;

                // Verify if 2 passages are created
                List<Passage> dbPassages = passageDaoUtils.getAllEntities();
                if (dbPassages.size() < 2) {
                    dbPassages = addPassages();
                }

                /*-- Mode Create --*/
                if (mode.equals("Create")) {
                    if (competitionNameExists(newName)) return;
                    // Add competition
                    currentCompetition = addNewCompetition(newName);
                    // Add passagePart
                    if (currentCompetition != null && dbPassages != null && dbPassages.size() == 2) {
                        addPassageParts(currentCompetition.getCompetitionId(), dbPassages.get(0).getPsgOrder());
                        addPassageParts(currentCompetition.getCompetitionId(), dbPassages.get(1).getPsgOrder());
                    }
                } else {
                    /*-- Mode Modify --*/
                    // Verify if competition's name already exists
                    if (!newName.equals(currentCompetition.getCompetitionName())){
                        if(competitionNameExists(newName)) return;
                        currentCompetition.setCompetitionName(newName);
                    }
                    // Update competition information
                    competitionDaoUtils.updateEntity(currentCompetition);
                    // Update nbGroups of competition
                    currentCompetition.setNbGroups(getEditTextsContents().size());
                    competitionDaoUtils.updateEntity(currentCompetition);
                    // Reset passagePart with new groups
                    List<PassagePart> removePsgParts = passagePartDaoUtils.getByQueryBuilder(PassagePartDao.Properties.CompetitionId.eq(currentCompetition.getCompetitionId()));
                    passagePartDaoUtils.deleteEntities(removePsgParts);
                    if (dbPassages != null) {
                        addPassageParts(currentCompetition.getCompetitionId(), dbPassages.get(0).getPsgOrder());
                        addPassageParts(currentCompetition.getCompetitionId(), dbPassages.get(1).getPsgOrder());
                    }
                }
                // Update nbGroups
                if (currentCompetition != null) {
                    currentCompetition.setNbGroups(getEditTextsContents().size());
                    competitionDaoUtils.updateEntity(currentCompetition);
                }
                jumpToCompetition();
                finish();
            }
        });

        annulCompetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToCompetition();
                finish();
            }
        });
    }
}
