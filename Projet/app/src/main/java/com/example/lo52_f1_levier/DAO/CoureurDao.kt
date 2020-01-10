package com.example.lo52_f1_levier.DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.example.lo52_f1_levier.model.Coureur
import com.example.lo52_f1_levier.model.CourseDbHelper
import com.example.lo52_f1_levier.model.Participe
import java.lang.Exception

/**
 * Management of the 'Coureur' table
 *
 * @constructor
 * TODO
 *
 * @param context
 */
class CoureurDao(context: Context) {
    val dbHelper = CourseDbHelper(context)
    /**
     * Allow to insert data in the table
     *
     * @param cname
     * @param surname
     * @return
     */
    fun insertCoureur(cname: String, surname: String): Long? {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Coureur.CoureurTable.NUMC, getLastId() + 1)
            put(Coureur.CoureurTable.CNAME, cname)
            put(Coureur.CoureurTable.SURNAME, surname)
        }

        return db?.insert(Coureur.CoureurTable.NAME, null, values)
    }

    /**
     * Allow to get  a coureur by it's Number
     *
     * @param titre
     * @return
     */
    fun getCoureur(titre:Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Coureur.CoureurTable.NUMC, Coureur.CoureurTable.CNAME,
            Coureur.CoureurTable.SURNAME)

        val selection = "${Coureur.CoureurTable.NUMC} = ?"
        val selectionArgs = arrayOf(titre.toString())

        val sortOrder = "${Coureur.CoureurTable.CNAME} DESC"

        return db.query(
            Coureur.CoureurTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }

    /**
     * Allow to get  a coureur  by it's ID
     *
     * @param ID : coureur id
     * @return
     */
    fun getCoureurByID(ID:Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Coureur.CoureurTable.NUMC, Coureur.CoureurTable.CNAME,
            Coureur.CoureurTable.SURNAME)

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())

        val sortOrder = "${Coureur.CoureurTable.CNAME} DESC"

        return db.query(
            Coureur.CoureurTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }

    /**
     * Allow to get  a coureur by it's Number
     *
     * @param numc
     * @return
     */
    fun getCoureurByNumc(numc:Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Coureur.CoureurTable.NUMC, Coureur.CoureurTable.CNAME,
            Coureur.CoureurTable.SURNAME)

        val selection = "${Coureur.CoureurTable.NUMC} = ?"
        val selectionArgs = arrayOf(numc.toString())

        val sortOrder = "${Coureur.CoureurTable.CNAME} DESC"

        return db.query(
            Coureur.CoureurTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }

    /**
     * Allow to get  all coureur
     *
     * @return
     */
    fun getAllCoureur(): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Coureur.CoureurTable.NUMC, Coureur.CoureurTable.CNAME,
            Coureur.CoureurTable.SURNAME)

        val sortOrder = "${Coureur.CoureurTable.CNAME} DESC"

        return db.query(
            Coureur.CoureurTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }

    /**
     * Allow to delete  a coureur by it's Number
     *
     * @param numc
     * @return rows deleted
     */
    fun deleteCoureur(numc: Int): Int {
        val db = dbHelper.writableDatabase
        val selection = "${Coureur.CoureurTable.NUMC} LIKE ?"
        val selectionArgs = arrayOf(numc.toString())
        val deletedRows = db.delete(Coureur.CoureurTable.NAME, selection, selectionArgs)
        return deletedRows
    }

    /**
     * Allow to update a coureur by it's Number
     *
     * @param oldNumc
     * @param numc
     * @param cname
     * @param surname
     * @return
     */
    fun updateCoureur(oldNumc: Int,numc: Int,cname: String, surname: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Coureur.CoureurTable.NUMC, numc)
            put(Coureur.CoureurTable.CNAME,cname)
            put(Coureur.CoureurTable.SURNAME,surname)
        }
        val selection = "${Coureur.CoureurTable.NUMC} LIKE ?"
        val selectionArgs = arrayOf(oldNumc.toString())
        return db.update(
            Coureur.CoureurTable.NAME,
            values,
            selection,
            selectionArgs)
    }

    /**
     * Allow to update  a coureur by it's ID
     *
     * @param ID
     * @param numc
     * @param cname
     * @param surname
     * @return
     */
    fun updateCoureurByID(ID: Int,numc: Int,cname: String, surname: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Coureur.CoureurTable.NUMC, numc)
            put(Coureur.CoureurTable.CNAME,cname)
            put(Coureur.CoureurTable.SURNAME,surname)
        }
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())
        return db.update(
            Coureur.CoureurTable.NAME,
            values,
            selection,
            selectionArgs)
    }

    /**
     * Allow to update  a coureur by it's Number
     *
     * @param ID
     * @param numc
     * @return
     */
    fun updateCoureurNumc(ID: Int, numc: Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Coureur.CoureurTable.NUMC, numc)
        }
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())
        return db.update(
            Coureur.CoureurTable.NAME,
            values,
            selection,
            selectionArgs)
    }

    /**
     * Allow to getlast number used
     * *
     * @return last coureur number used
     */
    fun getLastId() : Int {
        val db = dbHelper.writableDatabase

        var res = db.rawQuery("SELECT MAX(Numc) as maxId FROM " + Coureur.CoureurTable.NAME, null)
        res.moveToFirst()
        try{
            return (res.getString(0)).toInt()
        }catch(e: Exception){
            return -1
        }

    }

    /**
     * Allow to get all coureur not linked to a course
     *
     * @param ID : course ID
     * @return
     */
    fun getCoureurFree(ID : Int): Cursor? {
        val db = dbHelper.readableDatabase

        val query = "SELECT * FROM "+Coureur.CoureurTable.NAME+  " WHERE "+BaseColumns._ID+
                " NOT IN ( SELECT "+Participe.ParticipeTable.COUREUR_ID+" FROM "+Participe.ParticipeTable.NAME+
                " WHERE "+Participe.ParticipeTable.COURSE_ID+ " = "+ ID.toString() + ")" +
                " ORDER BY " + Coureur.CoureurTable.CNAME + ";"

        return db.rawQuery(query, null)

    }
}