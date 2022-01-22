package com.instabug.androidtask.data.local

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.instabug.androidtask.data.model.Word


class WordQueries(context: Context) {

    private val dbHelper = SqliteHelper(context)
    @Throws(SQLException::class)
    fun open() : SQLiteDatabase {
        return dbHelper.writableDatabase
    }
    fun saveWord(model: Word): Long{
        val db = open()

        val contentValues = ContentValues()
//        contentValues.put(COL_ID, model.id)
        contentValues.put(SqliteHelper.COL_NAME, model.name)
        contentValues.put(SqliteHelper.COL_QUANTITY, model.quantity)

        val success = db.insert(SqliteHelper.TBL_WORD, null, contentValues)
        close()

        return success
    }

    fun close(){
        dbHelper.close()
    }

    fun getWords(keyword: String = "", sortOrder: String = "DESC"): ArrayList<Word>{
        val mDatabase = dbHelper.readableDatabase
        val contentList: ArrayList<Word> = ArrayList()
        Log.v("Yoyo", sortOrder + "Here")
        val selectionQuery = if(keyword.isEmpty()){
            " "
        }else{
            " WHERE ${SqliteHelper.COL_NAME} LIKE '%$keyword%' "
        }
        val selectQuery = "SELECT * FROM ${SqliteHelper.TBL_WORD}$selectionQuery order by CAST(${SqliteHelper.COL_QUANTITY} AS INTEGER) $sortOrder"
        Log.v("Yoyo", selectQuery)


        val cursor = mDatabase.rawQuery(selectQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(SqliteHelper.COL_ID))
                val itemName = getString(getColumnIndexOrThrow(SqliteHelper.COL_NAME))
                val itemQuantity = getInt(getColumnIndexOrThrow(SqliteHelper.COL_QUANTITY))
                contentList.add(Word(itemId, itemName, itemQuantity))
            }
        }
        cursor.close()
        close()
        return contentList
    }

    fun deleteAllWords(){
        open().delete(SqliteHelper.TBL_WORD, null, null)
    }
//    @Throws(SQLException::class)
    fun saveWords(list: List<Word>){
        val mDatabase = dbHelper.writableDatabase
        mDatabase.beginTransaction()
        try {
            deleteAllWords()
            val values = ContentValues()
            for (word in list) {
                values.put(SqliteHelper.COL_NAME, word.name )
                values.put(SqliteHelper.COL_QUANTITY, word.quantity)
                mDatabase.insert(SqliteHelper.TBL_WORD, null, values)
            }
            mDatabase.setTransactionSuccessful()
        } finally {
            mDatabase.endTransaction()
            close()
        }
    }
}