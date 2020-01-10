package com.example.fgurlsdev.OtherActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fgurlsdev.Adapters.MyAutoCompleteTextAdapter;
import com.example.fgurlsdev.DaoUtils.CommonDaoUtils;
import com.example.fgurlsdev.DaoUtils.DaoUtilsCollection;
import com.example.fgurlsdev.Entity.Group;
import com.example.fgurlsdev.Entity.GroupDao;
import com.example.fgurlsdev.Entity.Runner;
import com.example.fgurlsdev.Entity.RunnerDao;
import com.example.fgurlsdev.MainActivity;
import com.example.fgurlsdev.R;

import java.util.ArrayList;
import java.util.List;

public class CreateGroup extends AppCompatActivity {
    private List<Runner> runners = new ArrayList<>();
    private Group modifyGroup;

    private CommonDaoUtils<Runner> runnerDaoUtils;
    private CommonDaoUtils<Group> groupDaoUtils;
    private EditText newGroupName;
    private Button annulBtn;
    private Button confirmBtn;
    private AutoCompleteTextView runner1;
    private AutoCompleteTextView runner2;
    private AutoCompleteTextView runner3;

    private String mode = "Create";

    private void setAutoCompleteText(int id) {
        MyAutoCompleteTextAdapter runnerSpinnerAdapter = new MyAutoCompleteTextAdapter<>(getApplicationContext(), Runner.class, runners);
        AutoCompleteTextView autoCompleteText = findViewById(id);
        autoCompleteText.setAdapter(runnerSpinnerAdapter);
    }

    private void setContent(long groupId) {
        modifyGroup = groupDaoUtils.getEntityById(groupId);
        newGroupName.setText(modifyGroup.getGroupName());

        if (modifyGroup.getRunnerId1() != null) {
            Runner runner = runnerDaoUtils.getEntityById(modifyGroup.getRunnerId1());
            if (runner != null) runner1.setText(runner.getNom());
        }
        if (modifyGroup.getRunnerId2() != null) {
            Runner runner = runnerDaoUtils.getEntityById(modifyGroup.getRunnerId2());
            if (runner != null) runner2.setText(runner.getNom());
        }
        if (modifyGroup.getRunnerId3() != null) {
            Runner runner = runnerDaoUtils.getEntityById(modifyGroup.getRunnerId3());
            if (runner != null) runner3.setText(runner.getNom());
        }
    }

    // Jump to group fragment
    private void jumpToGroup(){
        Intent intent = new Intent(CreateGroup.this, MainActivity.class);
        intent.putExtra("fragmentId", 2);
        startActivity(intent);
    }

    // Verify if group's name exists or not
    private boolean groupNameExists(String newName){
        if (groupDaoUtils.getByQueryBuilder(GroupDao.Properties.GroupName.eq(newName)).size() != 0) {
            Toast.makeText(getApplicationContext(), "Nom d'equipe existe deja!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        newGroupName = findViewById(R.id.newGroupName);

        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
        runnerDaoUtils = daoUtilsCollection.getRunnerDaoUtils();
        groupDaoUtils = daoUtilsCollection.getGroupDaoUtils();
        runners = runnerDaoUtils.getByQueryBuilder(RunnerDao.Properties.HasGroup.eq(false));
        runner1 = findViewById(R.id.spinnerRunner1);
        runner2 = findViewById(R.id.spinnerRunner2);
        runner3 = findViewById(R.id.spinnerRunner3);

        // Set AutoCompleteText
        setAutoCompleteText(R.id.spinnerRunner1);
        setAutoCompleteText(R.id.spinnerRunner2);
        setAutoCompleteText(R.id.spinnerRunner3);

        // If require to modify group
        long groupId = getIntent().getLongExtra("groupId", -1);
        if (groupId != -1) {
            setContent(groupId);
            mode = "Modify";
        } else mode = "Create";

        annulBtn = findViewById(R.id.annulAddGroup);
        annulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("Modify")) {
                    // Reset runners' status
                    if (modifyGroup.getRunnerId1() != null){
                        Runner r1 = runnerDaoUtils.getEntityById(modifyGroup.getRunnerId1());
                        if (r1 != null) {
                            r1.setHasGroup(true);
                            runnerDaoUtils.updateEntity(r1);
                        }
                    }
                    if (modifyGroup.getRunnerId2() != null) {
                        Runner r2 = runnerDaoUtils.getEntityById(modifyGroup.getRunnerId2());
                        if (r2 != null) {
                            r2.setHasGroup(true);
                            runnerDaoUtils.updateEntity(r2);
                        }
                    }
                    if (modifyGroup.getRunnerId3() != null) {
                        Runner r3 = runnerDaoUtils.getEntityById(modifyGroup.getRunnerId3());
                        if (r3 != null) {
                            r3.setHasGroup(true);
                            runnerDaoUtils.updateEntity(r3);
                        }
                    }
                }
                jumpToGroup();
            }
        });

        confirmBtn = findViewById(R.id.confirmAddGroup);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get new name of group
                EditText editGroupName = findViewById(R.id.newGroupName);
                String newGroupName = editGroupName.getText().toString();

                Group newGroup;
                List<Runner> dbRunners;
                Runner dbR1 = new Runner();
                Runner dbR2 = new Runner();
                Runner dbR3 = new Runner();

                String runnerName1 = runner1.getText().toString();
                String runnerName2 = runner2.getText().toString();
                String runnerName3 = runner3.getText().toString();

                // Verify if there is a name duplicate
                if (!runnerName1.equals("")) {
                    if (runnerName1.equals(runnerName2) || runnerName1.equals(runnerName3)) {
                        Toast.makeText(getApplicationContext(), runnerName1 + " est duplique!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // Update runner hasGroup = true
                    dbRunners = runnerDaoUtils.getByQueryBuilder(RunnerDao.Properties.Nom.eq(runnerName1));
                    if (dbRunners == null || dbRunners.size() < 1){
                        Toast.makeText(getApplicationContext(), runnerName1 + " n\'existe pas!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    dbR1 = dbRunners.get(0);
                    if (dbR1.getHasGroup()) {
                        Toast.makeText(getApplicationContext(), runnerName1 + " est deja dans une autre equipe!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (!runnerName2.equals("")) {
                    if (runnerName2.equals(runnerName3)) {
                        Toast.makeText(getApplicationContext(), runnerName2 + " est duplique!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    dbRunners = runnerDaoUtils.getByQueryBuilder(RunnerDao.Properties.Nom.eq(runnerName2));
                    if (dbRunners == null || dbRunners.size() < 1){
                        Toast.makeText(getApplicationContext(), runnerName2 + " n\'existe pas!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    dbR2 = dbRunners.get(0);
                    if (dbR2.getHasGroup()) {
                        Toast.makeText(getApplicationContext(), runnerName2 + " est deja dans une autre equipe!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (!runnerName3.equals("")) {
                    dbRunners = runnerDaoUtils.getByQueryBuilder(RunnerDao.Properties.Nom.eq(runnerName3));
                    if (dbRunners == null || dbRunners.size() < 1){
                        Toast.makeText(getApplicationContext(), runnerName3 + " n\'existe pas!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    dbR3 = dbRunners.get(0);
                    if (dbR3.getHasGroup()) {
                        Toast.makeText(getApplicationContext(), runnerName3 + " est deja dans une autre equipe!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                // Update runner in database
                if(dbR1.getRunnerId() != null){
                    dbR1.setHasGroup(true);
                    runnerDaoUtils.updateEntity(dbR1);
                }
                if(dbR2.getRunnerId() != null){
                    dbR2.setHasGroup(true);
                    runnerDaoUtils.updateEntity(dbR2);
                }
                if(dbR3.getRunnerId() != null){
                    dbR3.setHasGroup(true);
                    runnerDaoUtils.updateEntity(dbR3);
                }

                /*-- Mode modify --*/
                if (mode.equals("Modify")) {
                    newGroup = modifyGroup;
                    if (!newGroupName.equals(modifyGroup.getGroupName()) && groupNameExists(newGroupName)) return;
                    if (runnerName1.equals("")){
                        // Name is null, remove a runner
                        if (newGroup.getRunnerId1() != null) newGroup.setNbMembers(newGroup.getNbMembers()-1);
                        newGroup.setRunnerId1(null);
                    } else {
                        // Name is not null, add a runner or modify the runner
                        if (newGroup.getRunnerId1() == null) newGroup.setNbMembers(newGroup.getNbMembers()+1);
                        // Update group
                        newGroup.setRunnerId1(dbR1.getRunnerId());
                    }
                    if (runnerName2.equals("")){
                        if (newGroup.getRunnerId2() != null) newGroup.setNbMembers(newGroup.getNbMembers()-1);
                        newGroup.setRunnerId2(null);
                    } else {
                        if (newGroup.getRunnerId2() == null) newGroup.setNbMembers(newGroup.getNbMembers()+1);
                        newGroup.setRunnerId2(dbR2.getRunnerId());
                    }
                    if (runnerName3.equals("")){
                        if (newGroup.getRunnerId3() != null) newGroup.setNbMembers(newGroup.getNbMembers()-1);
                        newGroup.setRunnerId3(null);
                    } else {
                        if (newGroup.getRunnerId3() == null) newGroup.setNbMembers(newGroup.getNbMembers()+1);
                        newGroup.setRunnerId3(dbR3.getRunnerId());
                    }
                    // Update group in database
                    groupDaoUtils.updateEntity(newGroup);
                } else {
                /*-- Mode create --*/
                    if(groupNameExists(newGroupName)) return;

                    newGroup = new Group(null, newGroupName, null, null, null, 0);

                    if (!runnerName1.equals("")) {
                        // Update group
                        newGroup.setNbMembers(newGroup.getNbMembers() + 1);
                        newGroup.setRunnerId1(dbR1.getRunnerId());
                    }
                    if (!runnerName2.equals("")) {
                        newGroup.setNbMembers(newGroup.getNbMembers() + 1);
                        newGroup.setRunnerId2(dbR2.getRunnerId());
                    }
                    if (!runnerName3.equals("")) {
                        newGroup.setNbMembers(newGroup.getNbMembers() + 1);
                        newGroup.setRunnerId3(dbR3.getRunnerId());
                    }
                    groupDaoUtils.insertEntity(newGroup);
                }
                jumpToGroup();
                finish();
            }
        });
    }
}