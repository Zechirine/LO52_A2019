package com.example.lo52_f1_levier.view

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lo52_f1_levier.DAO.CourseDao
import com.example.lo52_f1_levier.R
import kotlinx.android.synthetic.main.activity_create_course.*
import java.text.SimpleDateFormat
import java.util.*


class CreateCourseActivity : AppCompatActivity() {

    private lateinit var courseDao : CourseDao
    private lateinit var cal : Calendar

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_course)
        courseDao = CourseDao(this)
        setResult(Activity.RESULT_OK)

        cal = Calendar.getInstance()
        courseDate.setText(SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis()))
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
            courseDate.setText(sdf.format(cal.time))
        }

        courseDate.setOnClickListener {
            editDateClicked(dateSetListener)
        }

        courseDate.setOnFocusChangeListener{ view, hasFocus ->
            if (hasFocus) {
                editDateClicked(dateSetListener)
            }
        }

        createCourse.setOnClickListener {
            if(courseName.text.isNotEmpty() && courseDate.text.isNotEmpty()){
                courseDao.insertCourse(courseName.text.toString(),
                    courseDate.text.toString())
                finish()
            }
        }
    }

    private fun editDateClicked(dateSetListener : DatePickerDialog.OnDateSetListener)
    {
        DatePickerDialog(this, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
    }

}


