package com.instabug.androidtask.data.repository

import com.instabug.androidtask.data.model.Word


interface WordRepository {
    interface LoadWordsCallback {
        fun onWordsLoaded(words: List<Word>?)
        fun onDataNotAvailable()
        fun onError()
    }

    fun getWords(callback: LoadWordsCallback?, keyword: String, sortBy: String)
    fun saveWords(words: List<Word>?)
}