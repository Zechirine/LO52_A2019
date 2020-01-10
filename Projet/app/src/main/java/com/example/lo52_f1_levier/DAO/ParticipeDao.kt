package com.example.lo52_f1_levier.DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.example.lo52_f1_levier.model.Coureur
import com.example.lo52_f1_levier.model.Participe
import com.example.lo52_f1_levier.model.CourseDbHelper
import com.example.lo52_f1_levier.model.Equipe

/**
 * ensemble des fonction permettant de modifier le contenue de la table participe
 *
 * @constructor
 * TODO
 *
 * @param context
 */
class ParticipeDao(context : Context) {
    val dbHelper = CourseDbHelper(context)


    /**
     * Permet l'insertion dans la table participe
     * Allow to insert data in the table
     *
     * @param C_ID
     * @param CR_ID
     * @param E_ID
     * @return
     */
    fun insertParticipe(C_ID: Int, CR_ID: Int, E_ID : Int): Long? {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Participe.ParticipeTable.COUREUR_ID, CR_ID )
            put(Participe.ParticipeTable.COURSE_ID, C_ID)
            put(Participe.ParticipeTable.EQUIPE_ID, E_ID)
        }
        return db?.insert(Participe.ParticipeTable.NAME, null, values)
    }


    /**
     * Renvoie les enregistrements dont l'id du coureur = COUREURID
     * Renvoie les enregistrements dont l'id du coureur = CR_ID
     *
     * @param CR_ID
     * @return
     */
    fun getParticipeByRunnerIdRunId(CR_ID:Int,teamId: Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID, Participe.ParticipeTable.COUREUR_ID, Participe.ParticipeTable.COURSE_ID,
            Participe.ParticipeTable.EQUIPE_ID,
            Participe.ParticipeTable.TIME1,
            Participe.ParticipeTable.TIME2,
            Participe.ParticipeTable.TIME3,
            Participe.ParticipeTable.TIME4,
            Participe.ParticipeTable.TIME5,
            Participe.ParticipeTable.TIME6,
            Participe.ParticipeTable.TIME7,
            Participe.ParticipeTable.TIME8,
            Participe.ParticipeTable.TIME9,
            Participe.ParticipeTable.TIME10
            )

        val selection = "${Participe.ParticipeTable.COUREUR_ID} = ? AND ${Participe.ParticipeTable.COURSE_ID} = ?"
        val selectionArgs = arrayOf(CR_ID.toString(),teamId.toString())

        val sortOrder = "${Participe.ParticipeTable.EQUIPE_ID} ASC"

        return db.query(
            Participe.ParticipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }


    /**
     * Renvoie un enregistrement de la table participe  via leur ID
     *
     * @param ID
     * @return
     */
    fun getParticipeByID(ID:Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID, Participe.ParticipeTable.COUREUR_ID, Participe.ParticipeTable.COURSE_ID,
            Participe.ParticipeTable.EQUIPE_ID,
            Participe.ParticipeTable.TIME1,
            Participe.ParticipeTable.TIME2,
            Participe.ParticipeTable.TIME3,
            Participe.ParticipeTable.TIME4,
            Participe.ParticipeTable.TIME5,
            Participe.ParticipeTable.TIME6,
            Participe.ParticipeTable.TIME7,
            Participe.ParticipeTable.TIME8,
            Participe.ParticipeTable.TIME9,
            Participe.ParticipeTable.TIME10
        )

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())

        val sortOrder = "${Participe.ParticipeTable.EQUIPE_ID} DESC"

        return db.query(
            Participe.ParticipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }


    /**
     * Renvoie les enregistrements dont l'id de l'équipe  = E_ID
     *
     * @param E_ID
     * @return
     */
    fun getParticipeByE_ID(E_ID:Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID, Participe.ParticipeTable.COUREUR_ID, Participe.ParticipeTable.COURSE_ID,
            Participe.ParticipeTable.EQUIPE_ID,
            Participe.ParticipeTable.TIME1,
            Participe.ParticipeTable.TIME2,
            Participe.ParticipeTable.TIME3,
            Participe.ParticipeTable.TIME4,
            Participe.ParticipeTable.TIME5,
            Participe.ParticipeTable.TIME6,
            Participe.ParticipeTable.TIME7,
            Participe.ParticipeTable.TIME8,
            Participe.ParticipeTable.TIME9,
            Participe.ParticipeTable.TIME10
        )

        val selection = "${Participe.ParticipeTable.EQUIPE_ID} = ?"
        val selectionArgs = arrayOf(E_ID.toString())

        val sortOrder = "${Participe.ParticipeTable.COUREUR_ID} DESC"

        return db.query(
            Participe.ParticipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }

    /**
     * Renvoie les enregistrements dont l'id de la course  = C_ID
     *
     * @param C_ID
     * @return
     */
    fun getParticipeByC_ID(C_ID:Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID, Participe.ParticipeTable.COUREUR_ID, Participe.ParticipeTable.COURSE_ID,
            Participe.ParticipeTable.EQUIPE_ID,
            Participe.ParticipeTable.TIME1,
            Participe.ParticipeTable.TIME2,
            Participe.ParticipeTable.TIME3,
            Participe.ParticipeTable.TIME4,
            Participe.ParticipeTable.TIME5,
            Participe.ParticipeTable.TIME6,
            Participe.ParticipeTable.TIME7,
            Participe.ParticipeTable.TIME8,
            Participe.ParticipeTable.TIME9,
            Participe.ParticipeTable.TIME10
        )

        val selection = "${Participe.ParticipeTable.COURSE_ID} = ?"
        val selectionArgs = arrayOf(C_ID.toString())

        val sortOrder = Participe.ParticipeTable.EQUIPE_ID  // important for TeamBoxGridAdapter

        return db.query(
            Participe.ParticipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }


    /**
     * Renvoie les enregistrements dont l'id de la course  = C_ID et l'id de l'équipe =E_ID
     *
     * @param C_ID
     * @param E_ID
     * @return
     */
    fun getParticipeByC_ID_E_ID(C_ID:Int,E_ID: Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID, Participe.ParticipeTable.COUREUR_ID, Participe.ParticipeTable.COURSE_ID,
            Participe.ParticipeTable.EQUIPE_ID,
            Participe.ParticipeTable.TIME1,
            Participe.ParticipeTable.TIME2,
            Participe.ParticipeTable.TIME3,
            Participe.ParticipeTable.TIME4,
            Participe.ParticipeTable.TIME5,
            Participe.ParticipeTable.TIME6,
            Participe.ParticipeTable.TIME7,
            Participe.ParticipeTable.TIME8,
            Participe.ParticipeTable.TIME9,
            Participe.ParticipeTable.TIME10
        )

        val selection = "${Participe.ParticipeTable.COURSE_ID} = ? AND ${Participe.ParticipeTable.EQUIPE_ID} = ?"
        val selectionArgs = arrayOf(C_ID.toString(), E_ID.toString())

        val sortOrder = "${Participe.ParticipeTable.COUREUR_ID} DESC"

        return db.query(
            Participe.ParticipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }

    /**
     * TODO
     *
     * @param courseId
     * @return
     */
    fun getTeamsOfCourse(courseId: Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(Participe.ParticipeTable.EQUIPE_ID)

        val selection = "${Participe.ParticipeTable.COURSE_ID} = ?"
        val selectionArgs = arrayOf(courseId.toString())

        return db.query(
            Participe.ParticipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
    }


    /**
     * Supprime les enregistrements dont l'id du coureur = CR_ID
     *
     * @param CR_ID
     * @return
     */
    fun deleteParticipeByCR_ID(CR_ID: Int): Int {
        val db = dbHelper.writableDatabase
        val selection = "${Participe.ParticipeTable.COUREUR_ID} = ?"
        val selectionArgs = arrayOf(CR_ID.toString())
        val deletedRows = db.delete(Participe.ParticipeTable.NAME, selection, selectionArgs)
        return deletedRows
    }


    /**
     * deleteParticipeByCourseIdAndTeamId est utilisé pour supprimer des enregistrement dans la base
     * participe en focntion de l'id de la course et l'id de l'équipe
     *
     * @param C_ID
     * @param E_ID
     * @return
     */
    fun deleteParticipeByCourseIdAndTeamId(C_ID: Int, E_ID: Int): Int {
        val db = dbHelper.writableDatabase
        val selection = "${Participe.ParticipeTable.COURSE_ID} = ? AND ${Participe.ParticipeTable.EQUIPE_ID} = ?"
        val selectionArgs = arrayOf(C_ID.toString(),E_ID.toString())
        val deletedRows = db.delete(Participe.ParticipeTable.NAME, selection, selectionArgs)
        return deletedRows
    }


    /**
     * setTimeByRunnerId permet de definir le temps numTime du coureur CR_ID  a la valeur time
     *
     * @param CR_ID
     * @param numTime
     * @param time
     * @return
     */
    fun setTimeByRunnerId(CR_ID: Int, numTime : Int, time : Long): Int {
        val db = dbHelper.writableDatabase
        val values=ContentValues()
        when(numTime){
            1->  values.apply {
            put(Participe.ParticipeTable.TIME1, time)
            }
            2->  values.apply {
                put(Participe.ParticipeTable.TIME2, time)
            }
            3->  values.apply {
                put(Participe.ParticipeTable.TIME3, time)
            }
            4->  values.apply {
                put(Participe.ParticipeTable.TIME4, time)
            }
            5->  values.apply {
                put(Participe.ParticipeTable.TIME5, time)
            }
            6->  values.apply {
                put(Participe.ParticipeTable.TIME6, time)
            }
            7->  values.apply {
                put(Participe.ParticipeTable.TIME7, time)
            }
            8->  values.apply {
                put(Participe.ParticipeTable.TIME8, time)
            }
            9->  values.apply {
                put(Participe.ParticipeTable.TIME9, time)
            }
            10->  values.apply {
                put(Participe.ParticipeTable.TIME10, time)
            }
            else -> values.apply {
            put(Participe.ParticipeTable.TIME1, time)
            }
        }

        val selection = "${Participe.ParticipeTable.COUREUR_ID} = ?"
        val selectionArgs = arrayOf(CR_ID.toString())
        return db.update(
            Participe.ParticipeTable.NAME,
            values,
            selection,
            selectionArgs)
    }

    /**
     * Get and return the number of times and the total time saved for a given team in a given course
     * @param courseId the id of the course runned by the team
     * @param teamId the id of the team to get the times of
     * @return a pair of number with (nbTimes, lastTime)
     */
    fun getNbAndTotalTimeOfTeam(courseId: Int, teamId: Int): Pair<Int, Long> {
        val db = dbHelper.readableDatabase

        // assemble all the time columns
        val timeColumns = arrayOf(
            Participe.ParticipeTable.TIME1,
            Participe.ParticipeTable.TIME2,
            Participe.ParticipeTable.TIME3,
            Participe.ParticipeTable.TIME4,
            Participe.ParticipeTable.TIME5,
            Participe.ParticipeTable.TIME6,
            Participe.ParticipeTable.TIME7,
            Participe.ParticipeTable.TIME8,
            Participe.ParticipeTable.TIME9,
            Participe.ParticipeTable.TIME10
        )

        // select only the team times
        val selection = "${Participe.ParticipeTable.COURSE_ID} = ? AND ${Participe.ParticipeTable.EQUIPE_ID} = ?"
        val selectionArgs = arrayOf(courseId.toString(), teamId.toString())

        // get the times
        val timeCursor = db.query(
            Participe.ParticipeTable.NAME,
            timeColumns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        // if no time, return
        if (timeCursor == null || !timeCursor.moveToFirst()) {
            return Pair(-1, 0)
        }

        // check the times of all the team members
        var totalTime: Long = 0
        var nbTimes: Int = 0
        do {
            // get all the time values
            val times = timeColumns.map { timeColumn -> timeCursor.getLong(timeCursor.getColumnIndex(timeColumn)) }

            // calc last time
            totalTime += times.reduce { total, time  -> total + time }

            // calc nb times
            nbTimes += times.count { time -> time > 0 }
        } while (timeCursor.moveToNext())

        timeCursor.close()
        return Pair(nbTimes, totalTime)
    }

    /**
     * Get a runner by its team and position in the team
     *
     * @param teamId
     * @param numc
     * @return
     */
    fun getCoureurByTeamIdAndNumc(teamId: Int, numc: Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(Participe.ParticipeTable.COUREUR_ID)

        val selection = "${Participe.ParticipeTable.EQUIPE_ID} = ?"
        val selectionArgs = arrayOf(teamId.toString())

        val teamCursor = db.query(
            Participe.ParticipeTable.NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        var rIds = emptyArray<Int>()
        while (teamCursor.moveToNext()) {
            val rId = teamCursor.getInt(teamCursor.getColumnIndex(Participe.ParticipeTable.COUREUR_ID))
            rIds = rIds.plus(rId)
        }
        teamCursor.close()

        val queryIdsParameter = rIds.map { "?" }.joinToString(", ", "(", ")")
        val runnerSelection = "${BaseColumns._ID} IN $queryIdsParameter AND ${Coureur.CoureurTable.NUMC} = ?"
        val runnerSelectionArgs = rIds.map{ id -> id.toString() }.plus(numc.toString()).toTypedArray()
        return db.query(
            Coureur.CoureurTable.NAME,
            null, // The array of columns to return (pass null to get all)
            runnerSelection, // The columns for the WHERE clause
            runnerSelectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
    }

    /**
     * TODO
     *
     * @param courseId
     * @return
     */
    fun nextTeamPosition(courseId: Int): Int {
        val db = dbHelper.readableDatabase
        val teamListCursor = getTeamsOfCourse(courseId)

        if (teamListCursor === null || !teamListCursor.moveToFirst()) {
            return 1
        }

        var teamIds = emptyArray<Int>()
        while (teamListCursor.moveToNext()) {
            val tId = teamListCursor.getInt(teamListCursor.getColumnIndex(Participe.ParticipeTable.EQUIPE_ID))
            teamIds = teamIds.plus(tId)
        }

        val projection = arrayOf("MAX(${Equipe.EquipeTable.POSITION})")
        val queryIdsParameter = teamIds.map { "?" }.joinToString(", ", "(", ")")
        val selection = "${BaseColumns._ID} IN $queryIdsParameter"
        val selectionArgs = teamIds.map { id -> id.toString() }.toTypedArray()

        val posCursor = db.query(
            Equipe.EquipeTable.NAME,
            projection, // The array of columns to return (pass null to get all)
            selection, // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        posCursor.moveToFirst()
        val nextPos = posCursor.getInt(0) + 1
        posCursor.close()
        return nextPos
    }
}