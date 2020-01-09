package com.utbm.keepit;

import android.app.Application;
import android.content.Context;

import com.utbm.keepit.backend.dao.DaoMaster;

import com.utbm.keepit.backend.dao.DaoSession;
import com.utbm.keepit.backend.entity.User;

import org.greenrobot.greendao.database.Database;

public class MyApp extends Application {
    private static DaoSession daoSession;
    private static Context context;

    private static User loginUser;
    public static User getUser(){
        return loginUser;
    }
    public static void setUser(User u){
        loginUser = u;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "KeepIt");
        Database db = helper.getWritableDb();
//        this.initData(db); //执行过一次了，不需要再次执行
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getContext() {
        return context;
    }

    private void cleanDB(Database db){
        //TODO
    }

    private void initData(Database db){
        // init data in database
        //数据库中的数据名字和class中的属性名字不同   需要看 DAO中的 property
        System.out.println("------------------Update begin---------------------");
        db.execSQL("INSERT OR REPLACE INTO Topic(TOPIC_Name) VALUES('Dos')");
        db.execSQL("INSERT OR REPLACE INTO Topic(TOPIC_Name) VALUES('Cuisses')");
        db.execSQL("INSERT OR REPLACE INTO Topic(TOPIC_Name) VALUES('Biceps')");
        //插入 Exercise
        //    private Long id;
        //    private String name;
        //    private int typePublic; // 0/1/2  -->  adulte、jeune、les deux 适合成年人 未成年人 老人...
        //    private int levelDifficult;  // 动作难度
        //    private int levelGroup; // 0 1 2  运动的人的等级   入门  业余 专业运动员
        //    private String  description;
        //    private String imageResource;
        //TYPE_PUBLIC    LEVEL_DIFFICULT    LEVEL_GROUP  DESCRIPTION  IMAGE_RESOURCE
        db.execSQL("INSERT OR REPLACE INTO Exercise(_ID, NAME, TYPE_PUBLIC, LEVEL_DIFFICULT, LEVEL_GROUP, DESCRIPTION, IMAGE_RESOURCE) " +
                "VALUES(1,'foo',0,7,0,'descrpition 1',NULL)");
        db.execSQL("INSERT OR REPLACE INTO Exercise(_ID, NAME, TYPE_PUBLIC, LEVEL_DIFFICULT, LEVEL_GROUP, DESCRIPTION, IMAGE_RESOURCE) " +
                " VALUES(2,'bar',1,2,1,'descrpition 2',NULL)");
        db.execSQL("INSERT OR REPLACE INTO Exercise(_ID, NAME, TYPE_PUBLIC, LEVEL_DIFFICULT, LEVEL_GROUP, DESCRIPTION, IMAGE_RESOURCE) " +
                " VALUES(3,'xxxxx',2,5,2,'descrpition 3',NULL)");
        //插入JoinTopicExercise
//            private Long topicId;
//            private Long exerciseId;
        //   TOPIC_ID EXERCISE_ID
        db.execSQL("INSERT OR REPLACE INTO JOIN_TOPIC_EXERCISE(TOPIC_ID,EXERCISE_ID) VALUES('1','1')");
        db.execSQL("INSERT OR REPLACE INTO JOIN_TOPIC_EXERCISE(TOPIC_ID,EXERCISE_ID) VALUES('1','2')");
        db.execSQL("INSERT OR REPLACE INTO JOIN_TOPIC_EXERCISE(TOPIC_ID,EXERCISE_ID) VALUES('1','3')");
        db.execSQL("INSERT OR REPLACE INTO JOIN_TOPIC_EXERCISE(TOPIC_ID,EXERCISE_ID) VALUES('2','2')");
        db.execSQL("INSERT OR REPLACE INTO JOIN_TOPIC_EXERCISE(TOPIC_ID,EXERCISE_ID) VALUES('2','3')");
        //插入Seance
//            private Long id;
//            private Integer duration; // 持续时间
//            private Integer intensity;  // 强度
//            private Integer repeatTimes;
        db.execSQL("INSERT OR REPLACE INTO Seance(NAME,DURATION,INTENSITY,REPEAT_TIMES) VALUES('Seance A',10,5,3)");
        db.execSQL("INSERT OR REPLACE INTO Seance(NAME,DURATION,INTENSITY,REPEAT_TIMES) VALUES('Seance B',20,7,5)");
        //插入SeanceExercises
//            private Long seanceId;
//            private Long exerciseId;,
        db.execSQL("INSERT OR REPLACE INTO JOIN_SEANCE_EXERCISE(SEANCE_ID,EXERCISE_ID,DURATION,EXERCISE_ORDRE) VALUES(1,1,2,1)");
        db.execSQL("INSERT OR REPLACE INTO JOIN_SEANCE_EXERCISE(SEANCE_ID,EXERCISE_ID,DURATION,EXERCISE_ORDRE) VALUES(2,1,1,1)");
        db.execSQL("INSERT OR REPLACE INTO JOIN_SEANCE_EXERCISE(SEANCE_ID,EXERCISE_ID,DURATION,EXERCISE_ORDRE) VALUES(2,1,2,2)");
        db.execSQL("INSERT OR REPLACE INTO JOIN_SEANCE_EXERCISE(SEANCE_ID,EXERCISE_ID,DURATION,EXERCISE_ORDRE) VALUES(2,2,3,3)");
        //插入 User

        db.execSQL("INSERT OR REPLACE INTO User VALUES('admin','admin')");
        db.execSQL("INSERT OR REPLACE INTO User VALUES('admin2','admin2')");

        System.out.println("------------------Update end---------------------");
    }
}
