package com.tyro.birthdayreminder.custom_class

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import androidx.core.net.toUri

fun compressBitmap(bitmap: Bitmap, quality: Int = 40): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
    return stream.toByteArray()
}