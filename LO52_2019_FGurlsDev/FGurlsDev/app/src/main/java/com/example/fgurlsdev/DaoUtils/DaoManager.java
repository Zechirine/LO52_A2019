package com.example.fgurlsdev.DaoUtils;

import android.content.Context;

import com.example.fgurlsdev.BuildConfig;
import com.example.fgurlsdev.Entity.DaoMaster;
import com.example.fgurlsdev.Entity.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;


public class DaoManager {
    private static final String TAG = DaoManager.class.getSimpleName();
    private static final String DB_NAME = "fgurlsdev";

    private Context context;

    // Volatile : share in multiple threads
    private volatile static DaoManager daoManager = new DaoManager();
    private static DaoMaster daoMaster; // Appear only after at least one entity is created
    private static DaoMaster.DevOpenHelper devOpenHelper;
    private static DaoSession daoSession;

    public static DaoManager getInstance(){
        return daoManager;
    }

    // Open debug report
    public void setDebug(){
        if (BuildConfig.DEBUG){
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }

    private DaoManager(){
        setDebug();
    }

    public void init(Context context) { this.context = context; }

    // Verify if database already exists or not, if not, create it
    public DaoMaster getDaoMaster(){
        if(daoMaster == null) {
            DaoMaster.DevOpenHelper doHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
            daoMaster = new DaoMaster(doHelper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession(){
        if (daoSession == null){
            if(daoMaster == null) daoMaster = getDaoMaster();
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    // Close devOpenHelper
    public void closeHelper(){
        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }

    // Close DaoSession
    public void closeDaoSession(){
        if (daoSession != null){
            daoSession.clear();
            daoSession = null;
        }
    }

    // Close database's connection
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }
}
