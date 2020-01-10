package com.example.lo52_f1_levier.model

class Run (val id: Int,val name : String, val date : String, val over : Int){



    override fun toString() : String{
        return name + " : " + date
    }
}