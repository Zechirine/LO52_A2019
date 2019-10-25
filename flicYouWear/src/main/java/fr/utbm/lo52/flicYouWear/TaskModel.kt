package fr.utbm.lo52.flicYouWear

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskModel (val typeOfTask: String, val roomNumber: Number, var status: Boolean) : Parcelable {
}