package com.instabug.androidtask.data.repository

import android.content.Context
import com.instabug.androidtask.data.local.WordQueries
import com.instabug.androidtask.data.model.Word
import com.instabug.androidtask.data.repository.WordRepository.LoadWordsCallback
import com.instabug.androidtask.utils.DiskExecutor
import java.util.concurrent.Executor

class WordLocalDataSource private constructor(
    private val executor: Executor,
    private val wordQuery: WordQueries
) : WordDataSource.Local {
    override fun getWords(callback: LoadWordsCallback,keyword: String, sortBy: String) {
        val runnable = Runnable {
            val words = wordQuery.getWords(keyword, sortBy)
            if (words.isNotEmpty()) {
                callback.onWordsLoaded(words)
            } else {
                callback.onDataNotAvailable()
            }
        }
        executor.execute(runnable)
    }

    override fun saveWords(words: List<Word>?) {
        val runnable = Runnable { wordQuery.saveWords(words!!) }
        executor.execute(runnable)
    }

    companion object {
        private var instance: WordLocalDataSource? = null
        fun getInstance(context: Context): WordLocalDataSource? {
            if (instance == null) {
                instance = WordLocalDataSource(DiskExecutor(), WordQueries(context))
            }
            return instance
        }
    }
}