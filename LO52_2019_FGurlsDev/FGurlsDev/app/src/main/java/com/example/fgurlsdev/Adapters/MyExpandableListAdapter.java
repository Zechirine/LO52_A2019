package com.example.fgurlsdev.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fgurlsdev.OtherActivities.CreateCompetition;
import com.example.fgurlsdev.OtherActivities.CreateGroup;
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
import com.example.fgurlsdev.Entity.RunnerDao;
import com.example.fgurlsdev.OtherActivities.LaunchGameActivity;
import com.example.fgurlsdev.R;

import org.greenrobot.greendao.query.WhereCondition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyExpandableListAdapter<T> extends BaseExpandableListAdapter {
    private Context context;
    private ExpandableListView mView;
    private Class groupClass;
    private Class childClass;
    private List<T> listDataHeader;
    private HashMap<T, List<Object>> listHashMap;
    private CommonDaoUtils<Runner> runnerDaoUtils;
    private CommonDaoUtils<Group> groupDaoUtils;
    private CommonDaoUtils<Competition> competitionDaoUtils;
    private CommonDaoUtils<Passage> passageDaoUtils;
    private CommonDaoUtils<PassagePart> passagePartDaoUtils;

    public MyExpandableListAdapter(Context context, ExpandableListView mView, Class groupClass, Class childClass) {
        this.context = context;
        this.mView = mView;
        this.groupClass = groupClass;
        this.childClass = childClass;
        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
        runnerDaoUtils = daoUtilsCollection.getRunnerDaoUtils();
        groupDaoUtils = daoUtilsCollection.getGroupDaoUtils();
        competitionDaoUtils = daoUtilsCollection.getCompetitionDaoUtils();
        passageDaoUtils = daoUtilsCollection.getPassageDaoUtils();
        passagePartDaoUtils = daoUtilsCollection.getPassagePartDaoUtils();
        setList();
    }

    @SuppressWarnings("unchecked")
    private void setList(){
        this.listHashMap = new HashMap<>();
        this.listDataHeader = new ArrayList<>();
        if (groupClass.equals(Competition.class)) {
            // Don't show competition which is launched
            if(CompetitionDao.Properties.CompetitionDate.isNull() == null) return;
            this.listDataHeader = (List<T>) competitionDaoUtils.getByQueryBuilder(CompetitionDao.Properties.CompetitionDate.isNull());
            if (this.listDataHeader.size() == 0) return;
            List<PassagePart> passagePartsGroupByCompetitionAndGroup = passagePartDaoUtils.getByQueryBuilder(new WhereCondition.StringCondition("1 GROUP BY competitionId, groupName"));
            List<Object> groups;
            for (T competition : this.listDataHeader) {
                groups = new ArrayList<>();
                for (PassagePart passagePart : passagePartsGroupByCompetitionAndGroup) {
                    if (passagePart.getCompetitionId().equals(((Competition)competition).getCompetitionId())) {
                        List<Group> dbGroups = groupDaoUtils.getByQueryBuilder(GroupDao.Properties.GroupName.eq(passagePart.getGroupName()));
                        Group dbGroup = dbGroups.get(0);
                        groups.add(dbGroup);
                    }
                }
                this.listHashMap.put(competition, groups);
            }
        } else {
            this.listDataHeader = (List<T>) groupDaoUtils.getAllEntities();
            for (T group : this.listDataHeader) {
                List<Object> runners = new ArrayList<>();
                if(((Group)group).getRunnerId1() != null){
                    runners.add(runnerDaoUtils.getEntityById(((Group)group).getRunnerId1()));
                } else runners.add(new Runner());
                if(((Group)group).getRunnerId2() != null){
                    runners.add(runnerDaoUtils.getEntityById(((Group)group).getRunnerId2()));
                } else runners.add(new Runner());
                if(((Group)group).getRunnerId3() != null){
                    runners.add(runnerDaoUtils.getEntityById(((Group)group).getRunnerId3()));
                }  else runners.add(new Runner());
                this.listHashMap.put(group, runners);
            }
        }
    }

    // Jump to modify activity
    private void jumpToModifyActivity(long entityId){
        Intent intent;
        if(groupClass.equals(Group.class)){
            // Jump to modify group activity
            intent = new Intent(context, CreateGroup.class);
            intent.putExtra("groupId", entityId);
        } else {
            // Jump to modify competition activity
            intent = new Intent(context, CreateCompetition.class);
            intent.putExtra("competitionId", entityId);
        }
        context.startActivity(intent);
    }

    // Reset all runners' status
    private void resetRunnersStatusOfGroup(Group currentGroup) {
        // Reset runners hasGroup status
        List<Object> objects = listHashMap.get(currentGroup);
        List<Runner> runners = new ArrayList<>();
        if(childClass.equals(Runner.class) && objects != null){
            for (Object obj : objects) runners.add((Runner) obj);
        }
        if (runners.size() > 0) {
            for (Runner runner : runners) {
                if(runner != null && runner.getRunnerId() != null){
                    runner.setHasGroup(false);
                    runnerDaoUtils.updateEntity(runner);
                }
            }
        }
    }

    private boolean groupInCourse(String groupName){
        DaoUtilsCollection daoUtilsCollection = DaoUtilsCollection.getInstance();
        CommonDaoUtils<PassagePart> psgPartDaoUtils = daoUtilsCollection.getPassagePartDaoUtils();
        if (PassagePartDao.Properties.GroupName.eq(groupName) == null) return false;
        if (PassagePartDao.Properties.PartResult.isNull() == null) return false;
        List<PassagePart> passageParts = psgPartDaoUtils.getByQueryBuilder(PassagePartDao.Properties.GroupName.eq(groupName),
                PassagePartDao.Properties.PartResult.isNull(),
                new WhereCondition.StringCondition("1 GROUP BY competitionId"));
        if (passageParts == null || passageParts.size() <= 0) return false;
        new AlertDialog.Builder(context)
                .setTitle("Notification")
                .setMessage(groupName + " est actuellement dans une course, vous ne pouvez pas le supprimer.")
                .setNeutralButton("Ok", null).show();
        return true;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Object> children = listHashMap.get(listDataHeader.get(groupPosition));
        return (children == null ? -1 : children.size());
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Object> children = listHashMap.get(listDataHeader.get(groupPosition));
        return (children == null ? -1 : children.get(childPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        if(groupClass.equals(Group.class)) {
            Group currentGroup = (Group) getGroup(groupPosition);
            List<Group> groupsDB = groupDaoUtils.getByQueryBuilder(GroupDao.Properties.GroupName.eq(currentGroup.getGroupName()));
            if (groupsDB != null && groupsDB.size() == 1) return groupsDB.get(0).getGroupId();
        } else {
            Competition currentCompetition = (Competition) getGroup(groupPosition);
            List<Competition> competitionsDB = competitionDaoUtils.getByQueryBuilder(CompetitionDao.Properties.CompetitionName.eq(currentCompetition.getCompetitionName()));
            if (competitionsDB != null && competitionsDB.size() == 1) return competitionsDB.get(0).getCompetitionId();
        }
        return -1;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        if (childClass.equals(Runner.class)) {
            Runner currentRunner = (Runner) getChild(groupPosition, childPosition);
            List<Runner> runnersDB = runnerDaoUtils.getByQueryBuilder(RunnerDao.Properties.Nom.eq(currentRunner.getNom()));
            if (runnersDB != null && runnersDB.size() == 1) return runnersDB.get(0).getRunnerId();
        } else {
            Group currentGroup = (Group) getChild(groupPosition, childPosition);
            List<Group> groupsDB = groupDaoUtils.getByQueryBuilder(GroupDao.Properties.GroupName.eq(currentGroup.getGroupName()));
            if (groupsDB != null && groupsDB.size() == 1) return groupsDB.get(0).getGroupId();
        }
        return -1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final Group currentGroup;
        final Competition currentCompetition;
        final Competition tmpCompetition;
        String title;

        if (groupClass.equals(Group.class)){
            currentGroup = (Group) getGroup(groupPosition);
            currentCompetition = new Competition();
            tmpCompetition = currentCompetition;
            title = currentGroup.getGroupName() + " --- " + currentGroup.getNbMembers();
        } else {
            currentGroup = new Group();
            currentCompetition = (Competition) getGroup(groupPosition);
            tmpCompetition = currentCompetition;
            title = currentCompetition.getCompetitionName() + " --- " + currentCompetition.getNbGroups();
        }

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_groups, null);
        }
        final View changedView = convertView;
        final TextView listHeader = convertView.findViewById(R.id.listGroups);
        ImageView launchIcon = convertView.findViewById(R.id.launchIcon);
        ImageView modifyIcon = convertView.findViewById(R.id.modifyGroupIcon);
        ImageView deleteIcon = convertView.findViewById(R.id.deleteGroupIcon);

        if(groupClass.equals(Group.class)) {
            launchIcon.setVisibility(View.GONE);
        } else {
            launchIcon.setVisibility(View.VISIBLE);
            //if(currentCompetition.getCompetitionDate() != null && !currentCompetition.getCompetitionDate().equals("")) convertView.setBackgroundColor(Color.GRAY);
        }

        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(title);

        launchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCompetition.getNbGroups() < 1) {
                    new AlertDialog.Builder(context)
                            .setTitle("Notification")
                            .setMessage(currentCompetition.getCompetitionName() + " n'a pas assez de group pour lancer le jeu.")
                            .setNeutralButton("Ok", null).show();
                    return;
                }
                //cif (tmpCompetition.getCompetitionDate() != null && !tmpCompetition.getCompetitionDate().equals("")) return;
                // Change competition's launching time in database
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
                Date date = new Date(System.currentTimeMillis());
                currentCompetition.setCompetitionDate(simpleDateFormat.format(date));
                competitionDaoUtils.updateEntity(currentCompetition);
                //changedView.setBackgroundColor(Color.GRAY);
                Competition removeCompetition = (Competition) listDataHeader.remove(groupPosition);
                listHashMap.remove(removeCompetition);
                notifyDataSetChanged();
                // Jump to launch game
                Intent intent = new Intent(context, LaunchGameActivity.class);
                intent.putExtra("competitionId", currentCompetition.getCompetitionId());
                context.startActivity(intent);
            }
        });

        modifyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset runners hasGroup status
                if (currentGroup.getGroupId() != null){
                    if(groupInCourse(currentGroup.getGroupName())) return;
                    resetRunnersStatusOfGroup(currentGroup);
                    // Jump to group fragment
                    jumpToModifyActivity(currentGroup.getGroupId());
                } else {
                    //if (tmpCompetition.getCompetitionDate() != null && !tmpCompetition.getCompetitionDate().equals("")) return;
                    jumpToModifyActivity(tmpCompetition.getCompetitionId());
                }
            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove group from view
                //Toast.makeText(context, "removeGroup!", Toast.LENGTH_LONG).show();
                if (groupClass.equals(Group.class)) {
                    Group removeGroup = (Group) listDataHeader.get(groupPosition);
                    if(groupInCourse(removeGroup.getGroupName())) return;

                    listDataHeader.remove(groupPosition);
                    listHashMap.remove(removeGroup);

                    // Remove group from database
                    groupDaoUtils.deleteEntity(removeGroup);

                    // Update runner hasGroup status
                    if (removeGroup.getRunnerId1() != null) {
                        Runner updateRunner = runnerDaoUtils.getEntityById(removeGroup.getRunnerId1());
                        updateRunner.setHasGroup(false);
                        runnerDaoUtils.updateEntity(updateRunner);
                    }
                    if (removeGroup.getRunnerId2() != null) {
                        Runner updateRunner = runnerDaoUtils.getEntityById(removeGroup.getRunnerId2());
                        updateRunner.setHasGroup(false);
                        runnerDaoUtils.updateEntity(updateRunner);
                    }
                    if (removeGroup.getRunnerId3() != null) {
                        Runner updateRunner = runnerDaoUtils.getEntityById(removeGroup.getRunnerId3());
                        updateRunner.setHasGroup(false);
                        runnerDaoUtils.updateEntity(updateRunner);
                    }
                } else {
                    Competition removeCompetition = (Competition) listDataHeader.get(groupPosition);
                    //if (removeCompetition.getCompetitionDate() != null && !removeCompetition.getCompetitionDate().equals("")) return;
                    listDataHeader.remove(groupPosition);
                    listHashMap.remove(removeCompetition);

                    // Remove passagePart from database
                    List<PassagePart> passagePartsToDelete = passagePartDaoUtils.getByQueryBuilder(PassagePartDao.Properties.CompetitionId.eq(removeCompetition.getCompetitionId()));
                    passagePartDaoUtils.deleteEntities(passagePartsToDelete);
                    // Remove competition from database
                    competitionDaoUtils.deleteEntity(removeCompetition);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        Object obj = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_items, null);
        }

        TextView listChild = convertView.findViewById(R.id.listGroupItems);
        ImageView deleteIcon = convertView.findViewById(R.id.removeRunnerIcon);
        ImageView plusIcon = convertView.findViewById(R.id.plusIcon);

        // For group-runners list
        if (childClass.equals(Runner.class)) {
            Runner child = (Runner) obj;
            if (child == null || child.getRunnerId() == null) {
                listChild.setVisibility(View.GONE);
                deleteIcon.setVisibility(View.GONE);
                plusIcon.setVisibility(View.VISIBLE);
            } else {
                listChild.setVisibility(View.VISIBLE);
                deleteIcon.setVisibility(View.VISIBLE);
                plusIcon.setVisibility(View.GONE);

                listChild.setText(child.getNom());
            }

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove child from view & database
                    Group modifyGroup = (Group) listDataHeader.get(groupPosition);

                    if(groupInCourse(modifyGroup.getGroupName())) return;

                    List<Object> runners = listHashMap.remove(modifyGroup);
                    if (runners != null && runners.size() > 0) {
                        Runner removeRunner = (Runner) runners.get(childPosition);
                        runners.set(childPosition, null);
                        listHashMap.put(listDataHeader.get(groupPosition), runners);
                        switch (childPosition) {
                            case 0:
                                modifyGroup.setRunnerId1(null);
                                break;
                            case 1:
                                modifyGroup.setRunnerId2(null);
                                break;
                            case 2:
                                modifyGroup.setRunnerId3(null);
                                break;
                        }
                        modifyGroup.setNbMembers(modifyGroup.getNbMembers() - 1);
                        groupDaoUtils.updateEntity(modifyGroup);

                        // Update runner hasGroup status
                        removeRunner.setHasGroup(false);
                        runnerDaoUtils.updateEntity(removeRunner);
                    }
                    mView.collapseGroup(groupPosition);
                    mView.expandGroup(groupPosition);
                }
            });

            plusIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetRunnersStatusOfGroup((Group) getGroup(groupPosition));
                    jumpToModifyActivity(getGroupId(groupPosition));
                }
            });
        } else {
            // For Competition-groups list
            listChild.setVisibility(View.VISIBLE);
            /*if (((Competition) listDataHeader.get(groupPosition)).getCompetitionDate() != null && !((Competition) listDataHeader.get(groupPosition)).getCompetitionDate().equals("")) deleteIcon.setVisibility(View.GONE); else*/
            deleteIcon.setVisibility(View.VISIBLE);
            plusIcon.setVisibility(View.GONE);

            Group child = (Group) obj;
            if(child != null) listChild.setText(child.getGroupName());

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove child from view & database
                    Competition modifyCompetition = (Competition) listDataHeader.get(groupPosition);

                    List<Object> groups = listHashMap.remove(modifyCompetition);
                    if (groups != null && groups.size() > 0) {
                        Group removeGroup = (Group) groups.remove(childPosition);
                        listHashMap.put(listDataHeader.get(groupPosition), groups);
                        // Remove passagePart
                        List<PassagePart> passagePartList = passagePartDaoUtils.getByQueryBuilder(
                                PassagePartDao.Properties.CompetitionId.eq(modifyCompetition.getCompetitionId()),
                                PassagePartDao.Properties.GroupName.eq(removeGroup.getGroupName()));
                        passagePartDaoUtils.deleteEntities(passagePartList);
                        // NbGroups of competition -1
                        modifyCompetition.setNbGroups(modifyCompetition.getNbGroups() - 1);
                    }
                    mView.collapseGroup(groupPosition);
                    mView.expandGroup(groupPosition);
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
