package com.instabug.androidtask.data.local

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.instabug.androidtask.data.model.Word


class WordQueries(private val context: Context) {

    private val dbHelper = SqliteHelper(context)

    fun saveWord(model: Word): Long{
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues()
//        contentValues.put(COL_ID, model.id)
        contentValues.put(SqliteHelper.COL_NAME, model.name)
        contentValues.put(SqliteHelper.COL_QUANTITY, model.quantity)

        val success = db.insert(SqliteHelper.TBL_WORD, null, contentValues)
        db.close()

        return success
    }

    fun getWords(keyword: String = "", sortOrder: String = "DESC"): ArrayList<Word>{
        val contentList: ArrayList<Word> = ArrayList()
        Log.v("Yoyo", sortOrder + "Here")
        val selectionQuery = if(keyword.isEmpty()){
            " "
        }else{
            "WHERE ${SqliteHelper.COL_NAME} LIKE '%$keyword%' "
        }
        val selectQuery = "SELECT * FROM ${SqliteHelper.TBL_WORD}$selectionQuery order by CAST(${SqliteHelper.COL_QUANTITY} AS INTEGER) $sortOrder"
        Log.v("Yoyo", selectQuery)
        val db = dbHelper.readableDatabase
        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        val projection = arrayOf(
//            SqliteHelper.COL_ID,
//            SqliteHelper.COL_NAME,
//            SqliteHelper.COL_QUANTITY
//        )
//
//        // DESC vs ASC
//        // Filter results WHERE "title" = 'My Title'
//        val selection = "${SqliteHelper.COL_NAME} = ?"
//        val selectionArgs = arrayOf("My Name")

        // How you want the results sorted in the resulting Cursor

//        val cursor = db.query(
//            SqliteHelper.TBL_WORD,   // The table to query
//            projection,             // The array of columns to return (pass null to get all)
//            null,              // The columns for the WHERE clause
//            null,          // The values for the WHERE clause
//            null,                   // don't group the rows
//            null,                   // don't filter by row groups
//            "CAST(${SqliteHelper.COL_QUANTITY} AS INTEGER) $sortOrder"               // The sort order
//        )

        val cursor = db.rawQuery(selectQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(SqliteHelper.COL_ID))
                val itemName = getString(getColumnIndexOrThrow(SqliteHelper.COL_NAME))
                val itemQuantity = getInt(getColumnIndexOrThrow(SqliteHelper.COL_QUANTITY))
                contentList.add(Word(itemId, itemName, itemQuantity))
            }
        }
        cursor.close()

        Log.v("Yoyo", contentList.toString())

        return contentList
    }

    fun deleteAllWords(){
        val db = dbHelper.writableDatabase
        db.delete(SqliteHelper.TBL_WORD, null, null)
    }
    fun saveWords(list: List<Word>){
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            deleteAllWords()
            val values = ContentValues()
            for (word in list) {
                values.put(SqliteHelper.COL_NAME, word.name )
                values.put(SqliteHelper.COL_QUANTITY, word.quantity)
                db.insert(SqliteHelper.TBL_WORD, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}