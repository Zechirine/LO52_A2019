package com.example.projet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlin.collections.ArrayList

class MyButtonsListViewAdapter(private val context: Context, private val myButtons: ArrayList<MyButton>):BaseAdapter() {
    override fun getItem(position: Int): Any {
        return "TEST STRING"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return myButtons.size
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val buttonRowView = layoutInflater.inflate(R.layout.button_row, viewGroup, false)

        val buttonNameTextView = buttonRowView.findViewById<View>(R.id.buttonName) as TextView
        buttonNameTextView.text = myButtons[position].getName()

        val roomNumberTextView = buttonRowView.findViewById<View>(R.id.roomNumber) as TextView
        roomNumberTextView.text = myButtons[position].roomNumber.toString()

        return buttonRowView
    }
}