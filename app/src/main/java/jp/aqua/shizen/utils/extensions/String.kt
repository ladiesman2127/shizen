package jp.aqua.shizen.utils.extensions

fun String.clean(): String {
    val res = this.trim()
    return res
}

fun String.toHtmlImage(): String {
    return "<img src=\"$this\"></img>"
}

fun String.toHtmlAudio(): String {
    return "<audio controls><source src=\"$this\"></audio>"
}

fun String.toHtmlVideo(): String {
    return "<video controls><source src=\"$this\"></video>"
}