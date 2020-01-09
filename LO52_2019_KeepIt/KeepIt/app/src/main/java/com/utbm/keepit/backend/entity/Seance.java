package com.utbm.keepit.backend.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.utbm.keepit.backend.dao.DaoSession;
import com.utbm.keepit.backend.dao.ExerciseDao;
import com.utbm.keepit.backend.dao.SeanceDao;

@Entity
public class Seance {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private Integer duration; // 持续时间
    private Integer intensity;  // 强度
    private Integer repeatTimes; //  seance 的重复次数  比如 一天做三 遍同一组运动
    // seance 所在的日期

    @ToMany
    @JoinEntity(
            entity = JoinSeanceExercise.class,
            sourceProperty = "seanceId",
            targetProperty = "exerciseId"
    )
    private List<Exercise> listExercises;

    public Seance( String name, Integer duration, Integer intensity,
                  Integer repeatTimes, List<Exercise> listE) {
//        this.id = id;
        this.name = name;
        this.duration = duration;
        this.intensity = intensity;
        this.repeatTimes = repeatTimes;
        this.listExercises= listE; // 这里不是DeepCopy
    }

    public Seance( Integer duration, Integer intensity,
                   Integer repeatTimes, List<Exercise> listE) {
//        this.id = id;
        this.duration = duration;
        this.intensity = intensity;
        this.repeatTimes = repeatTimes;
        this.listExercises= listE; // 这里不是DeepCopy
    }


    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1418926000)
    private transient SeanceDao myDao;

    @Generated(hash = 235524268)
    public Seance(Long id, String name, Integer duration, Integer intensity,
            Integer repeatTimes) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.intensity = intensity;
        this.repeatTimes = repeatTimes;
    }

    @Generated(hash = 2117235493)
    public Seance() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getIntensity() {
        return this.intensity;
    }

    public void setIntensity(Integer intensity) {
        this.intensity = intensity;
    }

    public Integer getRepeatTimes() {
        return this.repeatTimes;
    }

    public void setRepeatTimes(Integer repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 74149425)
    public List<Exercise> getListExercises() {
        if (listExercises == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExerciseDao targetDao = daoSession.getExerciseDao();
            List<Exercise> listExercisesNew = targetDao
                    ._querySeance_ListExercises(id);
            synchronized (this) {
                if (listExercises == null) {
                    listExercises = listExercisesNew;
                }
            }
        }
        return listExercises;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 961630282)
    public synchronized void resetListExercises() {
        listExercises = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 362389540)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSeanceDao() : null;
    }

}
