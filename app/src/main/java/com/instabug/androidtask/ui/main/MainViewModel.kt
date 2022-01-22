package com.instabug.androidtask.ui.main

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.MutableLiveData
import com.instabug.androidtask.data.model.Word
import com.instabug.androidtask.data.repository.WordRepository
import com.instabug.androidtask.data.repository.WordRepository.LoadWordsCallback
import com.instabug.androidtask.ui.base.BaseViewModel

class MainViewModel internal constructor(private val wordRepository: WordRepository?) :
    BaseViewModel() {

    val wordsLiveData = MutableLiveData<List<Word>?>()
    val showErrorMessageLiveData = MutableLiveData<String?>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val onlineLiveData = MutableLiveData(true)

    var sortBy: String = "DESC"
    private val wordCallback = WordCallback()

    init {
        loadWords()
    }

    fun loadWords(keyword: String = "") {
        setIsLoading(true)
        wordRepository!!.getWords(wordCallback, keyword, sortBy)
    }


    private fun setIsLoading(loading: Boolean) {
        loadingLiveData.postValue(loading)
    }

    private fun setWordsLiveData(wordList: List<Word>?) {
        setIsLoading(false)
        this.wordsLiveData.postValue(wordList)
    }

    /**
     * Callback
     */
    private inner class WordCallback : LoadWordsCallback {
        override fun onWordsLoaded(words: List<Word>?) {
            setWordsLiveData(words)
        }

        override fun onDataNotAvailable() {
            setIsLoading(false)
            showErrorMessageLiveData.postValue("There is not items!")
        }

        override fun onError() {
            setIsLoading(false)
            showErrorMessageLiveData.postValue("Something Went Wrong!")
        }
    }

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onlineLiveData.postValue(true)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            onlineLiveData.postValue(false)
        }
    }

}