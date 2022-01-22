package com.instabug.androidtask.data.repository

import com.instabug.androidtask.data.model.Word

interface WordDataSource {
    interface Remote {
        fun getWords(callback: WordRepository.LoadWordsCallback, keyword: String, sortBy: String)
    }

    interface Local : Remote {
        fun saveWords(words: List<Word>?)
    }
}