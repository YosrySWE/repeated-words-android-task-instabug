package com.instabug.androidtask

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.instabug.androidtask.data.model.Word
import com.instabug.androidtask.data.repository.WordRemoteDataSource
import com.instabug.androidtask.data.repository.WordRepository
import com.instabug.androidtask.utils.BASE_URL

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.instabug.androidtask", appContext.packageName)
    }

    @Test
    fun testRemoteRepo(){
//        BASE_URL = "https://www.google.com"
        val remoteRepo = WordRemoteDataSource.getInstance()
        remoteRepo?.getWords(object : WordRepository.LoadWordsCallback {
            override fun onWordsLoaded(words: List<Word>?) {
                assertTrue(words!!.isNotEmpty())
            }

            override fun onDataNotAvailable() {

            }

            override fun onError() {

            }
        }, "", "")
    }
}