package com.example.lo52_f1_levier.DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.example.lo52_f1_levier.model.Course
import com.example.lo52_f1_levier.model.CourseDbHelper

/**
 * Management of course table
 *
 * @constructor
 * TODO
 *
 * @param context
 */
class CourseDao(context: Context) {
    val dbHelper = CourseDbHelper(context)

    /**
     * Allow to insert data in the table
     *
     * @param titre
     * @param date
     * @return
     */
    fun insertCourse(titre: String, date: String): Long? {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Course.Coursetable.TITLE, titre)
            put(Course.Coursetable.DATE, date)
        }
        return db?.insert(Course.Coursetable.TABLE_NAME, null, values)
    }

    /**
     * Get course with it's title
     *
     * @param titre : title of the course
     * @return cursor
     */
    fun getCourse(titre:String): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Course.Coursetable.TITLE, Course.Coursetable.DATE, Course.Coursetable.OVER)

        val selection = "${Course.Coursetable.TITLE} = ?"
        val selectionArgs = arrayOf(titre)

        val sortOrder = "${Course.Coursetable.DATE} DESC"

        return db.query(
            Course.Coursetable.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }

    /**
     * Get course with it's ID
     *
     * @param ID : ID  of the course
     * @return cursor
     */
    fun getCourseByID(ID:Int): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Course.Coursetable.TITLE, Course.Coursetable.DATE, Course.Coursetable.OVER)

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())

        val sortOrder = "${Course.Coursetable.DATE} DESC"

        return db.query(
            Course.Coursetable.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }

    /**
     * Get all courses  of the table
     *
     * @return cursor
     */
    fun getAllCourse(): Cursor? {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Course.Coursetable.TITLE, Course.Coursetable.DATE, Course.Coursetable.OVER)


        val sortOrder = "${Course.Coursetable.DATE} DESC"

        return db.query(
            Course.Coursetable.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
    }

    /**
     * delete  course with it's title
     *
     * @param titre : titel of the course
     * @return number of row deleted
     */
    fun deleteCourse(titre: String): Int {
        val db = dbHelper.writableDatabase
        val selection = "${Course.Coursetable.TITLE} = ?"
        val selectionArgs = arrayOf(titre)
        val deletedRows = db.delete(Course.Coursetable.TABLE_NAME, selection, selectionArgs)
        return deletedRows
    }

    /**
     * Modify  course by it's title
     *
     * @param oldTitle
     * @param title
     * @param date
     * @return
     */
    fun updateCourse(oldTitle: String,title: String,date: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Course.Coursetable.TITLE, title)
            put(Course.Coursetable.DATE,date)
        }
        val selection = "${Course.Coursetable.TITLE} = ?"
        val selectionArgs = arrayOf(oldTitle)
        return db.update(
            Course.Coursetable.TABLE_NAME,
            values,
            selection,
            selectionArgs)
    }

    /**
     * Modify  course by it's ID
     *
     * @param ID
     * @param title
     * @param date
     * @return
     */
    fun updateCourseByID(ID: Int,title: String,date: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Course.Coursetable.TITLE, title)
            put(Course.Coursetable.DATE,date)
        }
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())
        return db.update(
            Course.Coursetable.TABLE_NAME,
            values,
            selection,
            selectionArgs)
    }

    /**
     * Set a course to be over
     */
    fun setCourseOver(ID: Int) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply { put(Course.Coursetable.OVER, 1) }

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())

        db.update(
            Course.Coursetable.TABLE_NAME,
            values,
            selection,
            selectionArgs)
    }

    /**
     * Check if a course is over
     */
    fun isCourseOver(ID: Int): Boolean {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(Course.Coursetable.OVER)

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID.toString())

        val cursor = db.query(
            Course.Coursetable.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        if (cursor == null || !cursor.moveToFirst()) {
            return false
        }

        return cursor.getInt(cursor.getColumnIndex(Course.Coursetable.OVER)) == 1
    }
}