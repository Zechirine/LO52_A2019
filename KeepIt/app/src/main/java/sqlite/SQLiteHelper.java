package sqlite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper{

    /*SQLiteHelper的四个参数，上下文，数据库名字，null,版本号（任意数字）*/
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*上面那个太过复杂，所以需要重载一个简单的方法：通过构造方法，完成数据库的创建*/
    public SQLiteHelper(Context context){
        super(context,"keepItPX",null,1);

    }

    /*通过OnCreate方法，实现数据表的创建*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table login (username varchar2(20) primary key, pwd varchar2(20)," +
                "height integer(3) ,weight decimal(3.2)," +
                "age integer(3), level integer(1))");

        System.out.println(db.getVersion());
        db.execSQL("insert into login values('admin','admin', 180, 75.5, 25, 3)");
        db.execSQL("insert into login values('admin2','admin2', 100, 75.5, 52, 5)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("create table login (username varchar2(20) primary key, pwd varchar2(20)," +
                "height integer(3) ,weight decimal(3.2)," +
                "age integer(3), level integer(1))");

        System.out.println("--------------------------------------database update--------------------------------------------------------");
        db.execSQL("insert into login values('admin','admin', 180, 75.5, 25, 3)");
        db.execSQL("insert into login values('admin2','admin2', 100, 75.5, 52, 5)");

    }
}