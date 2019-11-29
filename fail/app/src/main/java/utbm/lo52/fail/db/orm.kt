package utbm.lo52.fail.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.ColorSpace
import android.util.Log
import android.view.Display
import java.util.logging.Logger
import kotlin.reflect.KProperty
import kotlin.reflect.typeOf

abstract class ModelClass

class ForeignKey(val related: String, var id: Int)

class Player(val id: Int, var name: String, order: Int, val team: ForeignKey) : ModelClass()

class Team(val id: Int, var name: String, val race: ForeignKey) : ModelClass()

class Race(val id: Int, var name: String, var lap: Int) : ModelClass()

class Lap(val id: Int, var time: Int, val type: ForeignKey, val player: ForeignKey) : ModelClass()

class Type(val id: Int, var label: String) : ModelClass()

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    @ExperimentalStdlibApi
    override fun onCreate(db: SQLiteDatabase) {
        Log.v("INFO", "CREATING DATABASE")
        for (model in MODELS) {
            var table = "CREATE TABLE ${model.simpleName?.toLowerCase()} ("
            for (member in model.members) {
                if (member !is KProperty<*>) {
                    continue
                }
                if (member.name == "id") {
                    table += "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    continue
                }
                table += when (member.returnType) {
                    typeOf<Int>() -> "${member.name} INTEGER NOT NULL,"
                    typeOf<String>() -> " ${member.name} TEXT NOT NULL,"
                    typeOf<ForeignKey>() -> " ${member.name}_id NOT NULL,"
                    else -> ""
                }
            }
            table = "${table.substring(0, table.length - 1)})"
            Log.v("INFO", "Creating table with command : `$table`")
            db.execSQL(table)
        }
    }

    fun saveRace(race: Race) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", race.name)
        values.put("lap", race.lap)
        this.writableDatabase.insert("race", null, values)
    }

    fun getAllRaces(): List<Race> {
        val cursor = this.writableDatabase.rawQuery("SELECT * FROM race", null)
        val list = ArrayList<Race>()
        cursor!!.moveToFirst()
        list.add(
            Race(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getInt(cursor.getColumnIndex("lap"))
            )
        )
        while (cursor.moveToNext()) {
            list.add(
                Race(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("lap"))
                )
            )
        }
        return list
    }
//    fun saveModel(model: ModelClass){
//        for (member in model::class.members){
//
//        }
//    }

    @ExperimentalStdlibApi
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.v("INFO", "Upgrading Database")
        for (model in MODELS) {
            db.execSQL("DROP TABLE IF EXISTS ${model.simpleName}")
            Log.v("INFO", "Droping table ${model.simpleName}")
        }
        onCreate(db)
    }

    companion object {
        private val MODELS =
            listOf(Player::class, Team::class, Race::class, Lap::class, Type::class)
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "fail.db"
    }
}

