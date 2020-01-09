package com.utbm.keepit.backend.service;

import android.util.Log;

import com.utbm.keepit.MyApp;
import com.utbm.keepit.backend.dao.DaoSession;
import com.utbm.keepit.backend.dao.TopicDao;
import com.utbm.keepit.backend.entity.Topic;

import java.util.List;

import static android.content.ContentValues.TAG;

public class TopicService {
    private TopicDao topicDao;

    public TopicService(){
        DaoSession daoSession = MyApp.getDaoSession();
        topicDao = daoSession.getTopicDao();
    }
    //CRUD
    public boolean createTopic(Topic topic){
        boolean flag = false;
        try{
            topicDao.insert(topic);
            flag = true;
        }catch (Exception e){
            Log.i(TAG, "createTopic: "+e.toString());
            flag=false;
        }finally {
            return flag;
        }
    }

    public Topic findTopicById(Long id){
        return topicDao.load(id);
    }

    public List<Topic> findAll(){
        return topicDao.loadAll();
    }

//    public Topic findTopicByName(String name){
//        return topicDao.
//
//    }

    //TODO: 删除所有相关的EXERCIESE还是 只是删除所有相关的  JoinTopicExercise
    public boolean deleteTopic(Topic topic){
        boolean flag = false;
        try{
            topicDao.delete(topic);
            flag = true;
        }catch (Exception e){
            Log.i(TAG, "createTopic: "+e.toString());
            flag=false;
        }finally {
            return flag;
        }
    }
}
