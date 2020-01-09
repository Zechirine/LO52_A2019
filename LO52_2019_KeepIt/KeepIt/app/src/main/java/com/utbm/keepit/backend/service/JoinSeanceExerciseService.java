package com.utbm.keepit.backend.service;

import android.util.Log;

import com.utbm.keepit.MyApp;
import com.utbm.keepit.backend.dao.DaoSession;
import com.utbm.keepit.backend.dao.JoinSeanceExerciseDao;
import com.utbm.keepit.backend.entity.JoinSeanceExercise;
import static android.content.ContentValues.TAG;

public class JoinSeanceExerciseService {
    private JoinSeanceExerciseDao jseDao;

    public JoinSeanceExerciseService(){
        DaoSession daoSession = MyApp.getDaoSession();
        jseDao = daoSession.getJoinSeanceExerciseDao();
    }
    //CRUD
    public boolean createJoinSeanceExercise(JoinSeanceExercise jse){
        boolean flag = false;
        try{
            jseDao.insert(jse);
            flag = true;
        }catch (Exception e){
            Log.i(TAG, "createExercise: "+e.toString());
            flag=false;
        }finally {
            return flag;
        }
    }
}
