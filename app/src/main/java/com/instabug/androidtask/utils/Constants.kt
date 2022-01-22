package com.instabug.androidtask.utils

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.instabug.androidtask.data.model.Word
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern

var BASE_URL = "https://www.instabug.com"


fun MutableList<String>.fetchWords(): MutableList<Word>{
    if(this.isEmpty()){
        return mutableListOf()
    }else{
        val list: MutableList<Word> = mutableListOf()
        val map: HashMap<String, Int> = hashMapOf()
        this.forEach {
            if (it.isNotEmpty()) {
                if (map.keys.contains(it)) {
                    val x = map[it]
                    map[it] = 1 + x!!
                } else {
                    map[it] = 1
                }
            }

        }

        for (c in map.keys) {
            list.add(Word(name = c, quantity = map[c]!!))
        }
        return list
    }
}
fun String.splitBySpace(): MutableList<String>{
    return if(this.isEmpty()){
        mutableListOf()
    }else{
        this.split(" ").toMutableList()
    }
}
fun String.parseHTML(): String {
    return if(this.isEmpty()){
        ""
    }else{
        val stopWords =
            Pattern.compile(
                "\\b(our|were|when|where|your|with|How|and|you|the|The|for|but|not|n\'t|was|have|that|thatll|there|theres|why|mine|my|oh|will|new|didnt|what|because|youre|through|others|soon|had|before|after|st|this|these|those|than|them|about|yeah|much|yourself|same|has|whenever|us|its|him|her|his|some|it|then|all|ALL|each|do|does|who|we|re.|more|less|like|to|please|better|just|cant|can|nt|did|a|every|werent|lot|of)\\b\\s*",
                Pattern.CASE_INSENSITIVE
            )
        val matcher = stopWords.matcher(
            Jsoup.parse(this).text().replace(" [A-Za-z]{1,2} ".toRegex(), " ")
                .replace("[^a-zA-Z0-9 ]".toRegex(), "")
        )

        matcher.replaceAll("").lowercase()
    }

}

fun InputStream.readStream(): String{
    val r = BufferedReader(InputStreamReader(this))
    val total: StringBuilder = StringBuilder()
    var line: String?
    while (r.readLine().also { line = it } != null) {
        total.append(line).append('\n')
    }

    return total.toString()
}

