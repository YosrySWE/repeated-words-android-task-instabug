package com.instabug.androidtask.data.repository

import android.util.Log
import com.instabug.androidtask.data.model.Word
import com.instabug.androidtask.data.repository.WordRepository.LoadWordsCallback
import com.instabug.androidtask.utils.*
import java.io.BufferedInputStream
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor

class WordRemoteDataSource private constructor(
    private val executor: Executor
) :
    WordDataSource.Remote {

    override fun getWords(callback: LoadWordsCallback, keyword: String, sortBy: String) {
        val runnable = Runnable {

            var result: String
            val url = URL(BASE_URL)

            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            try {
                urlConnection.doOutput = true
                urlConnection.setChunkedStreamingMode(0)

                val `in`: InputStream = BufferedInputStream(urlConnection.inputStream)
                result = `in`.readStream()

            }catch (e: Exception){
                result = ""
            } finally {
                urlConnection.disconnect()
            }

            val list: MutableList<Word> = result.parseHTML().splitBySpace().fetchWords()
            list.sortedBy { it.quantity }

            Log.v("Yoyo", "From Remote: $list")

            if (list.isNotEmpty()) {
                callback.onWordsLoaded(list)
            } else {
                callback.onDataNotAvailable()
            }
        }
        executor.execute(runnable)
    }

    companion object {
        private var instance: WordRemoteDataSource? = null
        fun getInstance(): WordRemoteDataSource? {
            if (instance == null) {
                instance = WordRemoteDataSource(DiskExecutor())
            }
            return instance
        }
    }
}