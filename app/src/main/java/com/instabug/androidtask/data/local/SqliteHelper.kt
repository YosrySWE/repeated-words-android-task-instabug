package com.instabug.androidtask.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_NAME = "instaTask.db"
        const val DATABASE_VERSION = 1
        const val TBL_WORD = "word_table"
        const val COL_ID = "id"
        const val COL_NAME ="name"
        const val COL_QUANTITY ="quantity"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TBL_WORD (" +
                    "$COL_ID INTEGER PRIMARY KEY," +
                    "$COL_NAME TEXT," +
                    "$COL_QUANTITY TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TBL_WORD"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


}