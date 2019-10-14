package com.example.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskModel (val typeOfTask: String, val roomNumber: Number, var status: Boolean) : Parcelable {
}