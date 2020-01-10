package com.example.lo52_f1_levier.model

import android.provider.BaseColumns
/*
Objects contenants les noms des champs des tables
 */
object Course {
    object Coursetable : BaseColumns {
        const val TABLE_NAME = "Course"
        const val TITLE = "title"
        const val DATE = "date"
        const val OVER = "over"
    }
}

object Coureur {
    object CoureurTable : BaseColumns {
        const val NAME = "Coureur"
        const val NUMC = "Numc"
        const val CNAME = "Cname"
        const val SURNAME = "Surname"
    }
}
object Equipe {
    object EquipeTable : BaseColumns {
        const val NAME = "Equipe"
        const val ENAME = "Ename"
        const val ENUM = "Enum"
        const val POSITION = "Position"
    }
}
object Participe {
    object ParticipeTable : BaseColumns {
        const val NAME = "Participe"
        const val COURSE_ID = "COURSE_ID"     // Course
        const val COUREUR_ID = "COUREURID"   // Coureur
        const val EQUIPE_ID = "EQUIPE_ID"
        const val TIME1 = "Time1"
        const val TIME2 = "Time2"
        const val TIME3 = "Time3"
        const val TIME4 = "Time4"
        const val TIME5 = "Time5"
        const val TIME6 = "Time6"
        const val TIME7 = "Time7"
        const val TIME8 = "Time8"
        const val TIME9 = "Time9"
        const val TIME10 = "Time10"
    }
}


