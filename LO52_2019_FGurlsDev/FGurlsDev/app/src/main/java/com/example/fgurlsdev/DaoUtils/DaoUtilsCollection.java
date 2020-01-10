package com.example.fgurlsdev.DaoUtils;

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

public class DaoUtilsCollection {
    private volatile static DaoUtilsCollection daoUtilsCollection = new DaoUtilsCollection();
    private CommonDaoUtils<Runner> runnerDaoUtils;
    private CommonDaoUtils<Group> groupDaoUtils;
    private CommonDaoUtils<Competition> competitionDaoUtils;
    private CommonDaoUtils<Passage> passageDaoUtils;
    private CommonDaoUtils<PassagePart> passagePartDaoUtils;

    public static DaoUtilsCollection getInstance() {
        return daoUtilsCollection;
    }

    private DaoUtilsCollection() {
        DaoManager daoManager = DaoManager.getInstance();
        RunnerDao runnerDao = daoManager.getDaoSession().getRunnerDao();
        runnerDaoUtils = new CommonDaoUtils<>(Runner.class, runnerDao);

        GroupDao groupDao = daoManager.getDaoSession().getGroupDao();
        groupDaoUtils = new CommonDaoUtils<>(Group.class, groupDao);

        CompetitionDao competitionDao = daoManager.getDaoSession().getCompetitionDao();
        competitionDaoUtils = new CommonDaoUtils<>(Competition.class, competitionDao);

        PassageDao passageDao = daoManager.getDaoSession().getPassageDao();
        passageDaoUtils = new CommonDaoUtils<>(Passage.class, passageDao);

        PassagePartDao passagePartDao = daoManager.getDaoSession().getPassagePartDao();
        passagePartDaoUtils = new CommonDaoUtils<>(PassagePart.class, passagePartDao);
    }

    public CommonDaoUtils<Runner> getRunnerDaoUtils(){
        return runnerDaoUtils;
    }

    public CommonDaoUtils<Group> getGroupDaoUtils(){
        return groupDaoUtils;
    }

    public CommonDaoUtils<Competition> getCompetitionDaoUtils(){ return competitionDaoUtils; }

    public CommonDaoUtils<Passage> getPassageDaoUtils(){
        return passageDaoUtils;
    }

    public CommonDaoUtils<PassagePart> getPassagePartDaoUtils(){
        return passagePartDaoUtils;
    }
}
