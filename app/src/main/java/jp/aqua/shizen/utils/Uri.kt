package jp.aqua.shizen.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun Uri.getFileName(context: Context): String {

    if (scheme == "content") {
        val cursor = context.contentResolver.query(this, null, null, null, null)
        cursor.use {
            if (cursor?.moveToFirst() == true) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0)
                    return cursor.getString(index)
            }
        }
    }
    return toString()
}