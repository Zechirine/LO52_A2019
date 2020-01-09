package com.utbm.keepit.backend.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.utbm.keepit.backend.dao.DaoSession;
import com.utbm.keepit.backend.dao.TopicDao;
import com.utbm.keepit.backend.dao.ExerciseDao;

@Entity(indexes = {
        @Index(value = "id, topicName", unique = true)
})
public class Topic {

    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String topicName;
    private String imagePath;

    @ToMany
    @JoinEntity(
            entity = JoinTopicExercise.class,
            sourceProperty = "topicId",
            targetProperty = "exerciseId"
    )
    private List<Exercise> listExercises;

    public String toEasyString(){
        return this.getId()+": "+this.getTopicName();
    }
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 694021448)
private transient TopicDao myDao;

@Generated(hash = 27546345)
public Topic(Long id, String topicName, String imagePath) {
    this.id = id;
    this.topicName = topicName;
    this.imagePath = imagePath;
}

    public Topic(String topicName, String imagePath) {
        this.topicName = topicName;
        this.imagePath = imagePath;
    }

@Generated(hash = 849012203)
public Topic() {
}

@Override
public String toString(){
    return "id: "+ this.id + "name: "+this.topicName;
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getTopicName() {
    return this.topicName;
}

public void setTopicName(String topicName) {
    this.topicName = topicName;
}

public String getImagePath() {
    return this.imagePath;
}

public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 2140214581)
public List<Exercise> getListExercises() {
    if (listExercises == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        ExerciseDao targetDao = daoSession.getExerciseDao();
        List<Exercise> listExercisesNew = targetDao
                ._queryTopic_ListExercises(id);
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
@Generated(hash = 1373867845)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getTopicDao() : null;
}
}
