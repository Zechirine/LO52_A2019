package com.utbm.keepit.backend.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.utbm.keepit.backend.dao.DaoSession;
import com.utbm.keepit.backend.dao.SeanceDao;
import com.utbm.keepit.backend.dao.TopicDao;
import com.utbm.keepit.backend.dao.ExerciseDao;

@Entity
public class Exercise {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String name;
    private int typePublic; // 0/1/2  -->  adulte、jeune、les deux 适合成年人 未成年人 老人...
    private int levelDifficult;  // 动作难度
    private int levelGroup; // 0 1 2  运动的人的等级   入门  业余 专业运动员
    private String  description;
    private String imageResource;

    @ToMany
    @JoinEntity(
            entity = JoinTopicExercise.class,
            sourceProperty = "exerciseId",
            targetProperty = "topicId"
    )
    private List<Topic> listTopic;

    @ToMany
    @JoinEntity(
            entity = JoinSeanceExercise.class,
            sourceProperty = "exerciseId",
            targetProperty = "seanceId"
    )
    private List<Seance> listSeance;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 796236971)
private transient ExerciseDao myDao;

@Generated(hash = 1288621676)
public Exercise(Long id, String name, int typePublic, int levelDifficult,
        int levelGroup, String description, String imageResource) {
    this.id = id;
    this.name = name;
    this.typePublic = typePublic;
    this.levelDifficult = levelDifficult;
    this.levelGroup = levelGroup;
    this.description = description;
    this.imageResource = imageResource;
}

@Generated(hash = 1537691247)
public Exercise() {
}

    @Override
    public String toString(){
        return " "+this.name +"\n" +
                " "+ExerciseDataToDesciption.descripPublic.get(this.getTypePublic()) +"\n"+
                " "+ExerciseDataToDesciption.descripGroup.get(this.getLevelGroup()) +"\n"+
                " "+ExerciseDataToDesciption.descripDifficult.get(this.getLevelDifficult());

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

public int getTypePublic() {
    return this.typePublic;
}

public void setTypePublic(int typePublic) {
    this.typePublic = typePublic;
}

public int getLevelDifficult() {
    return this.levelDifficult;
}

public void setLevelDifficult(int levelDifficult) {
    this.levelDifficult = levelDifficult;
}

public int getLevelGroup() {
    return this.levelGroup;
}

public void setLevelGroup(int levelGroup) {
    this.levelGroup = levelGroup;
}

public String getDescription() {
    return this.description;
}

public void setDescription(String description) {
    this.description = description;
}

public String getImageResource() {
    return this.imageResource;
}

public void setImageResource(String imageResource) {
    this.imageResource = imageResource;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 627778844)
public List<Topic> getListTopic() {
    if (listTopic == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        TopicDao targetDao = daoSession.getTopicDao();
        List<Topic> listTopicNew = targetDao._queryExercise_ListTopic(id);
        synchronized (this) {
            if (listTopic == null) {
                listTopic = listTopicNew;
            }
        }
    }
    return listTopic;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1005647983)
public synchronized void resetListTopic() {
    listTopic = null;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 568621833)
public List<Seance> getListSeance() {
    if (listSeance == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        SeanceDao targetDao = daoSession.getSeanceDao();
        List<Seance> listSeanceNew = targetDao._queryExercise_ListSeance(id);
        synchronized (this) {
            if (listSeance == null) {
                listSeance = listSeanceNew;
            }
        }
    }
    return listSeance;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1149395867)
public synchronized void resetListSeance() {
    listSeance = null;
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
@Generated(hash = 110345356)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getExerciseDao() : null;
}
}
