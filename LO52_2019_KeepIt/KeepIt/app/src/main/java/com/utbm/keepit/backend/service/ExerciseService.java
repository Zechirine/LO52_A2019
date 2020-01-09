package com.utbm.keepit.backend.service;

import android.database.Cursor;
import android.graphics.Paint;
import android.util.Log;

import com.utbm.keepit.MyApp;
import com.utbm.keepit.backend.dao.DaoSession;
import com.utbm.keepit.backend.dao.ExerciseDao;
import com.utbm.keepit.backend.entity.Exercise;
import com.utbm.keepit.backend.entity.JoinSeanceExercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ExerciseService {
    private ExerciseDao exerciseDao;

    public ExerciseService(){
        DaoSession daoSession = MyApp.getDaoSession();
        exerciseDao = daoSession.getExerciseDao();
    }
    //CRUD
    public long createExercise(Exercise exercise){
        long eId = -1;
        try{
            exerciseDao.insert(exercise);
            eId = exerciseDao.getKey(exercise);
        }catch (Exception e){
            Log.i(TAG, "createExercise: "+e.toString());
        }finally {
            return eId;
        }
    }

    public Exercise findExerciseById(Long id){
        return exerciseDao.load(id);
    }

    public List<Exercise> findAll(){
        return exerciseDao.loadAll();
    }

    public Exercise findByName(String name){
        try {
            DaoSession session= MyApp.getDaoSession();
            long fromId=-1;
            String strSql="select * from EXERCISE e " +
                    "where e.NAME = " + name ;
            Cursor c  = session.getDatabase().rawQuery(strSql,null);
            Exercise e = new Exercise();
            if(c.moveToFirst())
            {
                e.setId(c.getLong(c.getColumnIndex("_id")));
                e.setDescription(c.getString(c.getColumnIndex("DESCRIPTION")));
                e.setName(c.getString(c.getColumnIndex("NAME")));
                e.setLevelDifficult(c.getInt(c.getColumnIndex("LEVEL_DIFFICULT")));
                e.setTypePublic(c.getInt(c.getColumnIndex("TYPE_PUBLIC")));
                e.setLevelGroup(c.getInt(c.getColumnIndex("LEVEL_GROUP")));
                e.setImageResource(c.getString(c.getColumnIndex("IMAGE_RESOURCE")));
                c.close();
              }else{
                e = null;
            }
            return e;
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
        return null;
    }

    public List<Exercise> findByTopicId(long topicId){
        try {
            DaoSession session= MyApp.getDaoSession();
            long fromId=-1;
            String strSql="select * from EXERCISE e " +
                    "inner join JOIN_TOPIC_EXERCISE te on e._id = te.EXERCISE_ID " +
                    "where te.TOPIC_ID = " + topicId ;
                   // select * from EXERCISE e inner join JOIN_TOPIC_EXERCISE te on e._id = te.EXERCISE_ID where te.TOPIC_ID = 1
                    Cursor c  = session.getDatabase().rawQuery(strSql,null);
            ArrayList<Exercise> list = new ArrayList<Exercise>();
            while(c.moveToNext())
            {
                Exercise exercise= new Exercise();
                exercise.setId(c.getLong(c.getColumnIndex("_id")));
                exercise.setDescription(c.getString(c.getColumnIndex("DESCRIPTION")));
                exercise.setName(c.getString(c.getColumnIndex("NAME")));
                exercise.setLevelDifficult(c.getInt(c.getColumnIndex("LEVEL_DIFFICULT")));
                exercise.setTypePublic(c.getInt(c.getColumnIndex("TYPE_PUBLIC")));
                exercise.setLevelGroup(c.getInt(c.getColumnIndex("LEVEL_GROUP")));
                exercise.setImageResource(c.getString(c.getColumnIndex("IMAGE_RESOURCE")));
                list.add(exercise);
            }
            c.close();
            return list;
        }
        catch (Exception ex)
        {
            ex.getMessage();

        }
        return null;
    }

    //TODO: join class 的删除？
    public boolean deleteExercise(Exercise exercise){
        boolean flag = false;
        try{
            exerciseDao.delete(exercise);
            flag = true;
        }catch (Exception e){
            Log.i(TAG, "createExercise: "+e.toString());
            flag=false;
        }finally {
            return flag;
        }
    }
    public ArrayList<ExerciseWithJoinSeance> findBySceanceId(long SceanceId){
        try
        {
            DaoSession session= MyApp.getDaoSession();
            long fromId=-1;
            String strSql="select * from EXERCISE e " +
                    "inner join JOIN_SEANCE_EXERCISE te on e._id = te.EXERCISE_ID " +
                    "where te.SEANCE_ID = " + SceanceId +" ORDER BY EXERCISE_ORDRE";
            // select * from EXERCISE e inner join JOIN_TOPIC_EXERCISE te on e._id = te.EXERCISE_ID where te.TOPIC_ID = 1
            Cursor c  = session.getDatabase().rawQuery(strSql,null);
            //Map<Exercise, JoinSeanceExercise> datas = new HashMap<Exercise,JoinSeanceExercise>();
            ArrayList<ExerciseWithJoinSeance> list = new ArrayList<>();
            while(c.moveToNext())
            {
                Exercise exercise= new Exercise();
                exercise.setId(c.getLong(c.getColumnIndex("_id")));
                exercise.setDescription(c.getString(c.getColumnIndex("DESCRIPTION")));
                exercise.setName(c.getString(c.getColumnIndex("NAME")));
                exercise.setLevelDifficult(c.getInt(c.getColumnIndex("LEVEL_DIFFICULT")));
                exercise.setTypePublic(c.getInt(c.getColumnIndex("TYPE_PUBLIC")));
                exercise.setLevelGroup(c.getInt(c.getColumnIndex("LEVEL_GROUP")));
                exercise.setImageResource(c.getString(c.getColumnIndex("IMAGE_RESOURCE")));
                JoinSeanceExercise joinSeanceExercise = new JoinSeanceExercise();
                joinSeanceExercise.setDuration(c.getInt(c.getColumnIndex("DURATION")));
                joinSeanceExercise.setExerciseOrdre(c.getInt(c.getColumnIndex("EXERCISE_ORDRE")));
                list.add(new ExerciseWithJoinSeance(exercise,joinSeanceExercise));

//                list.add(exercise);
            }
            c.close();
            return list;
        }
        catch (Exception ex)
        {
            ex.getMessage();

        }
        return null;
    }
}
