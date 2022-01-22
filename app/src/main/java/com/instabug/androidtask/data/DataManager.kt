package com.instabug.androidtask.data

import android.content.Context
import com.instabug.androidtask.data.repository.WordLocalDataSource
import com.instabug.androidtask.data.repository.WordRemoteDataSource
import com.instabug.androidtask.data.repository.WordRepository
import com.instabug.androidtask.data.repository.WordsRepositoryImpl

class DataManager {
    lateinit var wordRepository: WordRepository

    companion object {
        private var sInstance: DataManager? = null

        @Synchronized
        fun getInstance(context: Context): DataManager {
            if (sInstance == null) {
                sInstance = DataManager().apply {
                    val wordRemote: WordRemoteDataSource =
                        WordRemoteDataSource.getInstance()!!
                    val wordLocal: WordLocalDataSource =
                        WordLocalDataSource.getInstance(context = context)!!
                    wordRepository = WordsRepositoryImpl.getInstance(wordRemote, wordLocal)!!
                }
            }
            return sInstance!!
        }
    }
}