package fr.utbm.lo52.flicYouAndroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MyTasksListViewAdapter(private val context: Context, private val myTasks: ArrayList<MyTask>): BaseAdapter() {
    override fun getItem(position: Int): Any {
        return "TEST STRING"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return myTasks.size
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val taskRowView = layoutInflater.inflate(R.layout.task_row, viewGroup, false)

        val taskNameTextView = taskRowView.findViewById<View>(R.id.taskName) as TextView
        taskNameTextView.text = myTasks[position].getName()

        val taskIdTextView = taskRowView.findViewById<View>(R.id.taskId) as TextView
        taskIdTextView.text = myTasks[position].getId().toString()

        val taskStateTextView = taskRowView.findViewById<View>(R.id.taskState) as TextView

        if (myTasks[position].getState() == 0)
            taskStateTextView.text = "pending"
        if (myTasks[position].getState() == 1)
            taskStateTextView.text = "in progress"
        if (myTasks[position].getState() == 2)
            taskStateTextView.text = "done"

        return taskRowView
    }
}