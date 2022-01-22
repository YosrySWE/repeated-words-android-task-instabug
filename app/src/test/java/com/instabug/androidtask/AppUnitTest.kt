package com.instabug.androidtask

import com.instabug.androidtask.utils.parseHTML
import com.instabug.androidtask.utils.splitBySpace
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AppUnitTest {

    @Test
    fun testParseHtml(){
        val html = "<body><p>Hello</p></body>"
        assertEquals("hello", html.parseHTML())
    }

    @Test
    fun testOneWordInList(){
        val html = "<body><p>Hello World !</p></body>"
        val wordList = html.parseHTML().splitBySpace()
        assertEquals("hello", wordList[0])
        assertEquals("world", wordList[1])

        assertTrue(!wordList.contains("!"))

    }

}