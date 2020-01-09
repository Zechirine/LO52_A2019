package com.utbm.keepit.backend.service;

import android.util.Log;

import com.utbm.keepit.MyApp;
import com.utbm.keepit.backend.dao.DaoSession;
import com.utbm.keepit.backend.dao.SeanceDao;
import com.utbm.keepit.backend.entity.Seance;

import java.util.List;

import static android.content.ContentValues.TAG;

public class SeanceService {
    private SeanceDao seanceDao;

    public SeanceService(){
        DaoSession daoSession = MyApp.getDaoSession();
        seanceDao = daoSession.getSeanceDao();
    }
    //CRUD
    public Long createSeance(Seance seance){
        Long newId = Long.valueOf(-1);
        try{
            seanceDao.insert(seance);
            newId = seanceDao.getKey(seance);
            System.out.println("newID: " + newId);
        }catch (Exception e){
            Log.i(TAG, "createSeance: "+e.toString());
        }finally {
            return newId;
        }
    }

    public Seance findSeanceById(Long id){
        return seanceDao.load(id);
    }

    public List<Seance> findAll(){
        return seanceDao.loadAll();
    }
    
    public boolean deleteSeance(Seance seance){
        boolean flag = false;
        try{
            seanceDao.delete(seance);
            flag = true;
        }catch (Exception e){
            Log.i(TAG, "createSeance: "+e.toString());
            flag=false;
        }finally {
            return flag;
        }
    }
    
}
