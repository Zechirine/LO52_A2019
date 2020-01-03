package fr.utbm.lo52.flicYouAndroid.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import fr.utbm.lo52.flicYouAndroid.App

const val DATABASE_NAME = "flicYou.db"
const val DATABASE_VERSION = 2

object DatabaseManager : OrmLiteSqliteOpenHelper(App.instance, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {

        try {

            TableUtils.createTableIfNotExists(connectionSource, Zone::class.java)
            TableUtils.createTableIfNotExists(connectionSource, Message::class.java)
            TableUtils.createTableIfNotExists(connectionSource, Caregiver::class.java)
            TableUtils.createTableIfNotExists(connectionSource, Room::class.java)
            TableUtils.createTableIfNotExists(connectionSource, Have::class.java)
            Log.i("DATABASE", "OnCreate Invoked")
        }catch (exception : Exception){
            Log.e("DATABASE", "Can't create Database",exception)
        }

    }

    override fun onUpgrade(
        database: SQLiteDatabase?,
        connectionSource: ConnectionSource?,
        oldVersion: Int,
        newVersion: Int
    ) {
        try {

            TableUtils.dropTable<Zone, Any>(connectionSource, Zone::class.java, true)
            TableUtils.dropTable<Message, Any>(connectionSource, Message::class.java, true)
            TableUtils.dropTable<Caregiver, Any>(connectionSource, Caregiver::class.java, true)
            TableUtils.dropTable<Room, Any>(connectionSource, Room::class.java, true)
            TableUtils.dropTable<Have, Any>(connectionSource, Have::class.java, true)
            Log.i("DATABASE", "OnUpdate Invoked")
            onCreate(database, connectionSource)

        }catch (exception : Exception){
            Log.e("DATABASE", "Can't update Database",exception)
        }

    }

}