package com.example.lo52_f1_levier.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lo52_f1_levier.DAO.CourseDao
import com.example.lo52_f1_levier.DAO.ParticipeDao
import com.example.lo52_f1_levier.R
import com.example.lo52_f1_levier.view.coursetimer.CourseTimerActivity
import com.example.lo52_f1_levier.model.Course
import com.example.lo52_f1_levier.model.Run
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_course.*
import kotlinx.android.synthetic.main.spinner_item.*
import kotlinx.android.synthetic.main.tab_list_content.*

class CourseFragment : Fragment() {
    private lateinit var courseDao: CourseDao
    private lateinit var adapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseDao = CourseDao(this.context!!)

        val runs = ArrayList<Run>()
        val cursor = courseDao.getAllCourse()

        // Get the list of runs that we want to display
        with(cursor!!){
            while (moveToNext()){
                val run = Run(getInt(getColumnIndexOrThrow(BaseColumns._ID)),
                    getString(getColumnIndexOrThrow(Course.Coursetable.TITLE)),
                    getString(getColumnIndexOrThrow(Course.Coursetable.DATE)),
                    getInt(getColumnIndexOrThrow(Course.Coursetable.OVER))
                )
                runs.add(run)
            }
        }

        adapter = CourseAdapter(this::redirectToCourseTimer)
        adapter.replaceItems(runs)
        listCourse.layoutManager = LinearLayoutManager(this.context)
        listCourse.adapter = adapter

        newCourse.setOnClickListener {
            val intent = Intent(context, CreateCourseActivity::class.java)
            startActivityForResult(intent, 10001)
        }
    }

    private fun redirectToCourseTimer(courseId: Int) {
        val intent = Intent(context, CourseTimerActivity::class.java)
        intent.putExtra("courseId", courseId)
        startActivity(intent)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        super.setUserVisibleHint(isVisibleToUser)

        // Refresh tab data:

        if (fragmentManager != null) {

            fragmentManager!!
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10001 && resultCode == Activity.RESULT_OK) {
            // recreate your fragment here
            fragmentManager!!
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit()
        }
    }

    class CourseAdapter(private val goToCourseTimer: (Int) -> Unit) : RecyclerView.Adapter<CourseAdapter.ViewHolder>(){
        private var courses = ArrayList<Run>()
        private lateinit var participeDao : ParticipeDao

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            participeDao = ParticipeDao(parent.context)
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.spinner_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val course = courses[position]
            holder.text1.text = course.name + " : " + course.date
            holder.text1.setOnClickListener {parent->
                if(isTeamRun(course.id)) {goToCourseTimer(course.id)}
                else
                {
                    Toast.makeText(parent.context, "Aucune équipe assignée à cette course", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // Load the data to display in the recylerView
        fun replaceItems(course: ArrayList<Run>) {
            this.courses = course
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return courses.size
        }

        private fun isTeamRun(runId:Int):Boolean
        {
            val cursorTeam = participeDao.getParticipeByC_ID(runId)
            if(cursorTeam!==null)
            {
                val thereIs =  cursorTeam.moveToNext()
                cursorTeam.close()
                return thereIs
            }
            return false
        }

        inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment CourseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseFragment().apply {
            }
    }
}
