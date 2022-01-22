package com.instabug.androidtask.data.remote

import android.os.AsyncTask
import com.instabug.androidtask.data.model.Word
import org.jsoup.Jsoup
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern


class ContentTask : AsyncTask<String, Void, MutableList<Word>>() {
    override fun doInBackground(vararg params: String?): MutableList<Word> {

        val url = URL(params[0])

        var result = ""
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        try {
            urlConnection.doOutput = true
            urlConnection.setChunkedStreamingMode(0)

            val `in`: InputStream = BufferedInputStream(urlConnection.inputStream)
            result = readStream(`in`)

        } finally {
            urlConnection.disconnect()
        }

        var str = html2text(result)!!
        val list: MutableList<Word> = mutableListOf()
        val map: HashMap<String, Int> = hashMapOf()

        str.lowercase().split(" ").toMutableList().forEach {
//            list.add(ContentModel(name = it))
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
//        return html2text(result)!!

    }

    fun readStream(inputStream: InputStream): String {
        val r = BufferedReader(InputStreamReader(inputStream))
        val total: StringBuilder = StringBuilder()
        var line: String?
        while (r.readLine().also { line = it } != null) {
            total.append(line).append('\n')
        }

        return total.toString()

    }

    fun html2text(html: String?): String? {
        val stopWords =
            Pattern.compile(
                "\\b(our|were|when|where|your|with|How|and|you|the|The|for|but|not|n\'t|was|have|that|thatll|there|theres|who|why|mine|my|oh|will|new|didnt|what|because|youre|through|others|soon|had|before|after|st|this|these|those|than|them|about|yeah|much|yourself|same|has|whenever|us|its|him|her|his|some|it|then|all|ALL|each|do|does|who|we|re.|more|less|like|to|please|better|just|cant|can|nt|did|a|every|werent|lot|of)\\b\\s*",
                Pattern.CASE_INSENSITIVE
            )
        val matcher = stopWords.matcher(
            Jsoup.parse(html!!).text().replace(" [A-Za-z]{1,2} ".toRegex(), " ")
                .replace("[^a-zA-Z0-9 ]".toRegex(), "")
        )

        return matcher.replaceAll("")
    }

    override fun onPostExecute(result: MutableList<Word>) {
        super.onPostExecute(result)
    }
}