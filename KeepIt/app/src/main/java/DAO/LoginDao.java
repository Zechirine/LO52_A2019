package DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import sqlite.SQLiteHelper;

public class LoginDao {
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db ;
    public LoginDao(Context context){
        sqLiteHelper = new SQLiteHelper(context);
    }

    /*登陆功能*/
    public boolean login(String username , String pwd){

// 当数据库中的数据进行查询操作的时候，需要调用getReadableDatabase()
        db = sqLiteHelper.getReadableDatabase();
        Cursor cursor =  db.query("login", new String[]{"username"},"username = ? and pwd = ?",new String[]{username,pwd},
                null,null,null);
        if (cursor.moveToNext()){
            cursor.close();
            db.close();
            return true;
        }else {
            return false;
        }
    }

    public Cursor getUserInfoByName(String name){

// 当数据库中的数据进行查询操作的时候，需要调用getReadableDatabase()
        db = sqLiteHelper.getReadableDatabase();
        //
        //table login (username varchar2(20) , pwd varchar2(20)," +
        //                "height integer(3) ,weight decimal(3.2)," +
        //                "age integer(3), level integer(1))");
        Cursor cursor =  db.query("login",
                new String[]{"username","height","weight","age","level"},
                "username = ?",
                new String[]{name},
                null,null,null);
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println(cursor.toString());

        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------------");
                return cursor;
    }


}
