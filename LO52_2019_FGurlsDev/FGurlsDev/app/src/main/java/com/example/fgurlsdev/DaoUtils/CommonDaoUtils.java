package com.example.fgurlsdev.DaoUtils;

import android.database.Cursor;
import android.util.Log;

import com.example.fgurlsdev.Entity.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

public class CommonDaoUtils<T> {
    private static final String TAG = CommonDaoUtils.class.getSimpleName();

    private DaoSession daoSession;
    private Class<T> entityClass;
    private AbstractDao<T, Long> entityDao;

    public CommonDaoUtils(Class<T> entityClass, AbstractDao<T, Long> entityDao){
        DaoManager daoManager = DaoManager.getInstance();
        daoSession = daoManager.getDaoSession();
        this.entityClass = entityClass;
        this.entityDao = entityDao;
    }

    // Get all runners
    public List<T> getAllEntities() {
        return daoSession.loadAll(entityClass);
    }

    // Get runner by ID
    public T getEntityById(long idEntity) {
        return daoSession.load(entityClass, idEntity);
    }

    // Insert a Runner
    public boolean insertEntity(T myEntity) {
        boolean flag = (entityDao.insert(myEntity) != -1);
        Log.i(TAG, "INSERT Entity : " + flag + " >> " + myEntity.toString());
        return flag;
    }

    // Insert multiple runner
    public boolean insertEntities(final List<T> myEntities) {
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T myEntity : myEntities) {
                        daoSession.insertOrReplace(myEntity);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Modify an Entity
    public boolean updateEntity(T myEntity){
        try {
            daoSession.update(myEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete an Entity
    public boolean deleteEntity(T myEntity) {
        try {
            daoSession.delete(myEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete some Entities
    public boolean deleteEntities(final List<T> myEntities) {
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T myEntity : myEntities) {
                        daoSession.delete(myEntity);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete all entities
    public boolean deleteAllEntities() {
        try {
            daoSession.deleteAll(entityClass);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Use native sql for searching
    public List<T> getByNativeSql(String sqlQuery, String[] conditions) {
        return daoSession.queryRaw(entityClass, sqlQuery, conditions);
    }

    // Use native sql for counting
    public Long getSumByNativeSql(String column, String tableName, String conditionLine) {
        String sqlQuery = "SELECT SUM(" + column + ") FROM " + tableName + " WHERE " + conditionLine;
        Cursor cursor = null;
        long count = 0;
        try {
            cursor = daoSession.getDatabase().rawQuery(sqlQuery, null);
            if (cursor == null || !cursor.moveToFirst()) { return ((long)-1); }
            count = cursor.getLong(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return count;
    }

    // Use queryBuilder for searching
    public List<T> getByQueryBuilder(WhereCondition condition, WhereCondition... moreCondition){
        QueryBuilder<T> queryBuilder = daoSession.queryBuilder(entityClass);
        return queryBuilder.where(condition, moreCondition).list();
    }

    public List<T> getAllAsc(Property result){
        QueryBuilder<T> queryBuilder = daoSession.queryBuilder(entityClass);
        return queryBuilder.orderAsc(result).list();
    }


    public T getUniqueByQuery(WhereCondition condition, WhereCondition... moreCondition) {
        QueryBuilder<T> queryBuilder = daoSession.queryBuilder(entityClass);
        return queryBuilder.where(condition, moreCondition).build().unique();
    }
}
