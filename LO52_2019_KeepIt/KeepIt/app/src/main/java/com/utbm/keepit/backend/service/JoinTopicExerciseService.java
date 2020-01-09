package com.utbm.keepit.backend.service;
import android.util.Log;

import com.utbm.keepit.MyApp;
import com.utbm.keepit.backend.dao.DaoSession;
import com.utbm.keepit.backend.dao.JoinTopicExerciseDao;
import com.utbm.keepit.backend.entity.JoinTopicExercise;

import static android.content.ContentValues.TAG;

public class JoinTopicExerciseService {
        private JoinTopicExerciseDao jteDao;

        public JoinTopicExerciseService(){
            DaoSession daoSession = MyApp.getDaoSession();
            jteDao = daoSession.getJoinTopicExerciseDao();
        }
        //CRUD
        public boolean createJoinTopicExercise (JoinTopicExercise jte){
            boolean flag = false;
            try{
                jteDao.insert(jte);
                flag = true;
            }catch (Exception e){
                Log.i(TAG, "createExercise: "+e.toString());
                flag=false;
            }finally {
                return flag;
            }
        }
}

