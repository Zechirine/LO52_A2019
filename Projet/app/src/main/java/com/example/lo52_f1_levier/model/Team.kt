package com.example.lo52_f1_levier.model

class Team (val id: Int,val name : String, val number : Int){

    override fun toString() : String{
        return name + " " +id
    }
}