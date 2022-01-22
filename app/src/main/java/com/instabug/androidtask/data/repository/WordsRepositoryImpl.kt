package com.instabug.androidtask.data.repository

import android.util.Log
import com.instabug.androidtask.data.model.Word
import com.instabug.androidtask.data.repository.WordRepository.LoadWordsCallback


class WordsRepositoryImpl private constructor(
    wordRemote: WordRemoteDataSource?,
    wordLocal: WordLocalDataSource?
) : WordRepository {
    private val wordRemote: WordDataSource.Remote?
    private val wordLocal: WordDataSource.Local?
    override fun getWords(callback: LoadWordsCallback?, keyword: String, sortBy: String) {
        if (callback == null) return
        wordLocal!!.getWords(object : LoadWordsCallback {
            override fun onWordsLoaded(words: List<Word>?) {
                Log.v("Yoyo", "From Local: $words")
                callback.onWordsLoaded(words)
            }

            override fun onDataNotAvailable() {
                getMoviesFromRemoteDataSource(callback, keyword, sortBy)
            }

            override fun onError() {
                //not implemented in local data source
            }
        },keyword, sortBy)
    }

    override fun saveWords(words: List<Word>?) {

        wordLocal!!.saveWords(words)
    }


    private fun getMoviesFromRemoteDataSource(callback: LoadWordsCallback, keyword: String, sortBy: String) {
        wordRemote!!.getWords(object : LoadWordsCallback {
            override fun onWordsLoaded(words: List<Word>?) {
                saveWords(words)
                Log.v("Yoyo", "From Remote Into Local: $words")
                callback.onWordsLoaded(words?.sortedBy { it.quantity })
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

            override fun onError() {
                callback.onError()
            }
        }, keyword, sortBy)
    }

    private fun refreshCache(words: List<Word>?) {
        wordLocal!!.saveWords(words)
    }

    companion object {
        private var instance: WordsRepositoryImpl? = null
        fun getInstance(
            wordRemote: WordRemoteDataSource?,
            wordLocal: WordLocalDataSource?
        ): WordsRepositoryImpl? {
            if (instance == null) {
                instance = WordsRepositoryImpl(wordRemote, wordLocal)
            }
            return instance
        }
    }

    init {
        this.wordRemote = wordRemote
        this.wordLocal = wordLocal
    }
}