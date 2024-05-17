package jp.aqua.shizen.read.book.extensions

import android.content.Context
import android.net.Uri
import com.mohamedrejeb.ksoup.entities.KsoupEntities
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlOptions
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubWriter
import nl.siegmann.epublib.service.MediatypeService
import java.io.File
import java.io.FileOutputStream
import java.text.BreakIterator
import java.util.UUID

private fun parseChapter(chapter: ByteArray): ByteArray {
    var body = false
    val parsedChapter = StringBuilder()
    val handler = KsoupHtmlHandler
        .Builder()
        .onText { text ->
            if (!body) {
                parsedChapter.append(text)
                return@onText
            }
            if (text.isBlank()) {
                parsedChapter.append(text)
                return@onText
            }
            if (text == "<" || text == ">")
                return@onText
            val text = text.replace('\n', ' ')
            val iterator = BreakIterator.getSentenceInstance()
            iterator.setText(text)
            var start: Int = iterator.first()
            var end: Int = iterator.next()
            while (end != BreakIterator.DONE) {
                parsedChapter.append("<sentence>")
                val sentence = text.substring(start, end)
                val words = sentence.split(' ').filter { it != " " && it.isNotBlank() }
                for (word in words) {
                    if (word != "<" && word != ">") {
                        if (!word.contains("&"))
                            parsedChapter.append("<word>$word</word> ")
                        else
                            parsedChapter.append("<word>${KsoupEntities.encodeHtml(word)}</word> ")
                    }
                    start = end
                    end = iterator.next()
                }
                parsedChapter.append("</sentence>")
            }
        }
        .onOpenTag { name, attributes, _ ->
            if (name == "body")
                body = true
            parsedChapter.append("<$name")
            attributes.forEach { (attribute, value) ->
                parsedChapter.append(
                    when (attribute) {
                        "preserveaspectratio" -> " preserveAspectRatio=\"$value\""
                        "viewbox" -> " viewBox=\"$value\""
                        else -> " $attribute=\"$value\""
                    }
                )
            }
            parsedChapter.append(">")
        }
        .onCloseTag { name, _ ->
            if (name == "body") {
                parsedChapter.append(getJS())
                body = false
            }
            parsedChapter.append("</$name>")
        }
        .build()

    val ksoupHtmlParser = KsoupHtmlParser(handler)
    ksoupHtmlParser.write(String(chapter))
    return parsedChapter.toString().toByteArray()
}

fun Book.parseChapters() {
    for (chapter in resources.getResourcesByMediaType(MediatypeService.XHTML))
        chapter.data = parseChapter(chapter.data)
}


/**
 * Parses book and stores it in the storageDir
 * @param storageDir Files directory
 * @return Directory of a stored book
 */
fun Book.storeParsedBook(storageDir: File): File {
    val booksDir = File(storageDir, "books/")
    if (!booksDir.exists())
        booksDir.mkdir()
    val bookDir = File(storageDir, "books/${UUID.randomUUID()}")
    bookDir.mkdir()
    parseChapters()
    val bookFile = File(bookDir, "book.epub")
    val outputStream = FileOutputStream(bookFile).apply { flush() }
    EpubWriter().write(this@storeParsedBook, outputStream)
    outputStream.close()
    return bookFile
}

suspend fun Book.storeCoverImage(coverDir: File): File =
    withContext(Dispatchers.IO) {
        val coverFile = File(coverDir, UUID.randomUUID().toString())
        FileOutputStream(coverFile).apply {
            flush()
            close()
        }
        coverFile.writeBytes(coverImage.data)
        coverFile
    }

private fun getJS(): String {
    return "    <script>\n" +
            "       var words = document.querySelectorAll('word')\n" +
            "       words.forEach(word => {\n" +
            "           word.addEventListener(\"click\", () => {\n" +
            "               android.onOpenWordDef(word.textContent, word.parentElement.textContent)\n" +
            "           })\n" +
            "       })\n" +
            "   </script>"
}