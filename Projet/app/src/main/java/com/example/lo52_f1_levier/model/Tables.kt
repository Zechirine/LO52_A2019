package com.example.lo52_f1_levier.model

import android.provider.BaseColumns
/*
* Scripte de création des tables
*
* */
const val SQL_CREATE_COURSE =
    "CREATE TABLE ${Course.Coursetable.TABLE_NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
        "${Course.Coursetable.TITLE} TEXT," +
        "${Course.Coursetable.DATE} TEXT," +
        "${Course.Coursetable.OVER} NUMBER DEFAULT 0" +
    ")"

const val SQL_DELETE_COURSE = "DROP TABLE IF EXISTS ${Course.Coursetable.TABLE_NAME}"

const val SQL_CREATE_EQUIPE =
    "CREATE TABLE ${Equipe.EquipeTable.NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
        "${Equipe.EquipeTable.ENUM} NUMBER,"+
        "${Equipe.EquipeTable.ENAME} TEXT," +
        "${Equipe.EquipeTable.POSITION} NUMBER DEFAULT 0" +
    ")"

const val SQL_DELETE_EQUIPE = "DROP TABLE IF EXISTS ${Equipe.EquipeTable.NAME}"

const val SQL_CREATE_COUREUR =
    "CREATE TABLE ${Coureur.CoureurTable.NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
        "${Coureur.CoureurTable.NUMC} NUMBER,"+
        "${Coureur.CoureurTable.CNAME} TEXT," +
        "${Coureur.CoureurTable.SURNAME} TEXT" +
    ")"

const val SQL_DELETE_COUREUR = "DROP TABLE IF EXISTS ${Coureur.CoureurTable.NAME}"

const val SQL_CREATE_PARTICIPE =
    "CREATE TABLE ${Participe.ParticipeTable.NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
        "${Participe.ParticipeTable.COURSE_ID} INTEGER," +
        "${Participe.ParticipeTable.COUREUR_ID} INTEGER,"+
        "${Participe.ParticipeTable.EQUIPE_ID} INTEGER,"+
        "${Participe.ParticipeTable.TIME1} NUMBER DEFAULT -1,"+
        "${Participe.ParticipeTable.TIME2} NUMBER DEFAULT -1,"+
        "${Participe.ParticipeTable.TIME3} NUMBER DEFAULT -1,"+
        "${Participe.ParticipeTable.TIME4} NUMBER DEFAULT -1,"+
        "${Participe.ParticipeTable.TIME5} NUMBER DEFAULT -1,"+
        "${Participe.ParticipeTable.TIME6} NUMBER DEFAULT -1,"+
        "${Participe.ParticipeTable.TIME7} NUMBER DEFAULT -1,"+
        "${Participe.ParticipeTable.TIME8} NUMBER DEFAULT -1,"+
        "${Participe.ParticipeTable.TIME9} NUMBER DEFAULT -1,"+
        "${Participe.ParticipeTable.TIME10} NUMBER DEFAULT -1" +
    ")"


const val SQL_DELETE_PARTICIPE = "DROP TABLE IF EXISTS ${Participe.ParticipeTable.NAME}"