package com.example.lo52_f1_levier.DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.example.lo52_f1_levier.model.Equipe
import com.example.lo52_f1_levier.model.CourseDbHelper
import com.example.lo52_f1_levier.model.Participe

/**
 * Management of the 'Equipe' table
 *
 * @constructor
 * TODO
 *
 * @param context
 */
class EquipeDao(context : Context) {
    val dbHelper = CourseDbHelper(context)
    /**
     * Allow to insert data in the table
     *
     * @param titre
     * @param enum
     * @return cursor
     */
    fun insertEquipe(titre: String, enum : Int): Long? {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Equipe.EquipeTable.ENAME, titre)
            put(Equipe.EquipeTable.ENUM, enum)
        }
        val newRowId = db?.insert(Equipe.EquipeTable.NAME, null, values)
        return newRowId
    }

    /**
     * Allow to get to get a team by it's name
     *
     * @param ename : team name
     * @return cursor
     */
    fun getEquipe(ename:String): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Equipe.EquipeTable.ENAME, Equipe.EquipeTable.ENUM)

        val selection = "${Equipe.EquipeTable.ENAME} = ?"
        val selectionArgs = arrayOf(ename)


        val cursor = db.query(
            Equipe.EquipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null
        )

        return cursor
    }

    /**
     * Allow to get  a team by it's ID
     *
     * @param ID
     * @return cursor
     */
    fun getEquipeByID(ID:Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            Equipe.EquipeTable.ENAME,
            Equipe.EquipeTable.ENUM,
            Equipe.EquipeTable.POSITION
        )

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())


        val cursor = db.query(
            Equipe.EquipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null
        )

        return cursor
    }

    /**
     * Allow to delete a team by it's name
     *
     * @param ename : team name
     * @return Int : count how many delete
     */
    fun deleteEquipe(ename: String): Int {
        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(ename)
        return db.delete(Equipe.EquipeTable.NAME, selection, selectionArgs)
    }

    /**
     * Allow to delete a team by it's ID
     *
     * @param ID
     * @return Int : count how many delete
     */
    fun deleteEquipeByID(ID: Int): Int {
        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(ID.toString())
        return db.delete(Equipe.EquipeTable.NAME, selection, selectionArgs)
    }

    /**
     * Allow to modify a team by giving it's name
     *
     * @param oldEname : old team name
     * @param ename : new team name
     * @param enum : numero d'équipe
     * @return Int : count how many change
     */
    fun updateEquipe(oldEname: String,ename: String, enum : Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Equipe.EquipeTable.ENAME, ename)
            put(Equipe.EquipeTable.ENUM, enum)
        }
        val selection = "${Equipe.EquipeTable.ENAME} LIKE ?"
        val selectionArgs = arrayOf(oldEname)
        val count = db.update(
            Equipe.EquipeTable.NAME,
            values,
            selection,
            selectionArgs)
        return count
    }

    /**
     * Allow to modify a team by giving it's ID
     *
     * @param ID : team ID
     * @param ename : new team name
     * @param enum : team number
     * @return Int : count how many change
     */
    fun updateEquipeByID(ID: Int,ename: String, enum : Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Equipe.EquipeTable.ENAME, ename)
            put(Equipe.EquipeTable.ENUM, enum)
        }
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(ID.toString())
        val count = db.update(
            Equipe.EquipeTable.NAME,
            values,
            selection,
            selectionArgs)
        return count
    }

    /**
     * Return all the team
     *
     * @return cursor of all the team
     */
    fun getAllEquipe(): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Equipe.EquipeTable.ENAME, Equipe.EquipeTable.ENUM)

        val cursor = db.query(
            Equipe.EquipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null
        )

        return cursor
    }

    /**
     * Return teams attempting a course
     *
     * @param ID : ID of a course
     * @return
     */
    fun getTeamForACourse(ID : Int): Cursor? {
        val db = dbHelper.readableDatabase

        var query = "SELECT * FROM "+Equipe.EquipeTable.NAME+ " WHERE "+BaseColumns._ID +
                " IN ( SELECT "+ Participe.ParticipeTable.EQUIPE_ID+" FROM "+Participe.ParticipeTable.NAME+
                " WHERE "+Participe.ParticipeTable.COURSE_ID+  " = "+ ID.toString() + ");"

        return db.rawQuery(query, null)

    }

    /**
     * Modify the position of a team
     *
     * @param ID : ID of the team
     * @param pos : nem position
     */
    fun updatePosition(ID: Int, pos: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply { put(Equipe.EquipeTable.POSITION, pos) }
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())
        db.update(
            Equipe.EquipeTable.NAME,
            values,
            selection,
            selectionArgs
        )
    }

}