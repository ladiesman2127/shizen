package jp.aqua.shizen.utils

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

object KsoupUtils {
    fun textContent(data: String?): String {
        if (data == null) {
            return ""
        }
        val res = StringBuilder()
        val textContentHandler = KsoupHtmlHandler.Builder()
            .onText { text ->
                res.append(text)
            }
            .build()
        val parser = KsoupHtmlParser(textContentHandler)
        parser.write(data)
        return String(res)
    }
}