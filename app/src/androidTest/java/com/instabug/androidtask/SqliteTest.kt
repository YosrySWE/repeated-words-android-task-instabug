package com.instabug.androidtask

import android.database.sqlite.SQLiteDatabase
import android.provider.Telephony.Mms.Rate
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.instabug.androidtask.data.local.WordQueries
import com.instabug.androidtask.data.model.Word
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class SqliteTest {
    var dbQuery: WordQueries = WordQueries(InstrumentationRegistry.getInstrumentation().targetContext)

    @Before
    fun setUp() {
        dbQuery.open()
//        database = dbQuery.open()
    }

    @After
    fun finish() {
        dbQuery.close()
    }

    @Test
    fun testPreConditions() {
        assertNotNull(dbQuery.open())
    }

    @Test
    @Throws(Exception::class)
    fun testShouldAddOneItem() {
        dbQuery.deleteAllWords()
        dbQuery.saveWord(Word(name = "Yousry", quantity = 12))
        val word: List<Word> = dbQuery.getWords()
        assertThat(word.size, `is`(1))
        assertTrue(word[0].name == "Yousry")
        assertTrue(word[0].quantity == 12)
    }

    @Test
    fun testDeleteAll() {
        dbQuery.deleteAllWords()
        val wordList: List<Word> = dbQuery.getWords()
        assertThat(wordList.size, `is`(0))
    }


    @Test
    fun testData() {
        dbQuery.deleteAllWords()
        dbQuery.saveWord(Word(name = "Yousry", quantity = 12))
        dbQuery.saveWord(Word(name = "Mido", quantity = 5))
        Thread.sleep(1000)
        dbQuery.saveWord(Word(name = "Ahmed", quantity = 3))
        dbQuery.saveWord(Word(name = "Badr", quantity = 1))

        val list: List<Word> = dbQuery.getWords()
        assertEquals("Yousry", list[0].name)
        assertEquals("Mido", list[1].name)
        assertEquals("Ahmed", list[2].name)
        assertEquals(12, list[0].quantity)
        assertEquals(5, list[1].quantity)
        assertEquals(1, list[3].quantity)
    }

    @Test
    fun testSearchedData(){
        val query = "mi"
        dbQuery.deleteAllWords()
        dbQuery.saveWord(Word(name = "Mido", quantity = 12))
        dbQuery.saveWord(Word(name = "Mino", quantity = 5))
        dbQuery.saveWord(Word(name = "yoyo", quantity = 1))

        val list: List<Word> = dbQuery.getWords(keyword = query)

        assertTrue(list.size == 2)
        assertEquals("Mido", list[0].name)
    }

    fun testListIsSorted(){
        val sortBy = "DESC"
        dbQuery.deleteAllWords()
        dbQuery.saveWord(Word(name = "Mido", quantity = 1))
        dbQuery.saveWord(Word(name = "Mino", quantity = 2))
        dbQuery.saveWord(Word(name = "Yoyo", quantity = 3))
        dbQuery.saveWord(Word(name = "Mandy", quantity = 4))


        val list: List<Word> = dbQuery.getWords(sortOrder = sortBy) // DESC

        assertEquals("Mido", list[3].name)
        assertEquals("Mino", list[2].name)
        assertEquals("Yoyo", list[1].name)
        assertEquals("Mandy", list[0].name)

    }

}