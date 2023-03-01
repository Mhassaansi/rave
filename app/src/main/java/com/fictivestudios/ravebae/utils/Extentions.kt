package com.fictivestudios.ravebae.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

//fun File.getPartMap(key: String): MultipartBody.Part {
//    val reqFile = this.asRequestBody(this.absolutePath.getMimeType().toMediaTypeOrNull())
//    return MultipartBody.Part.createFormData(key, this.name, reqFile)
//}


/**
 * Get JSON Request body
 * */
fun String.getJsonRequestBody() = this.toRequestBody("application/json".toMediaTypeOrNull())


/**
 * Get Form Data Body
 * */
fun String.getFormDataBody() = this.toRequestBody("multipart/form-data".toMediaTypeOrNull())


/**
 * Get Part Map
 * */
fun File.getPartMap(key: String): MultipartBody.Part {
    val reqFile = this.asRequestBody(this.absolutePath.getMimeType().toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(key, this.name, reqFile)
}


/**
 * Convert any data into json data
 * */
fun Any.convertGsonString(): String = Gson().toJson(this)


/**
 * Convert String into Data Class
 * */
inline fun <reified T> String.convertStringIntoClass(): T = Gson().fromJson(this, T::class.java)


/**
 * Get Mime Type ......
 * */
fun String.getMimeType(): String {
    return try {
        val mimeType = MimeTypeMap.getFileExtensionFromUrl(this)
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeType) ?: ""
    } catch (e: Exception) {
        ""
    }
}


/** Added Methods */
fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(isEnabled: Boolean) {
    setEnabled(isEnabled)
    alpha = if (isEnabled) 1f else 0.5f
}

fun Context.toast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.showPermissionRequestDialog(
    title: String,
    body: String,
    callback: () -> Unit
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title)
        it.setMessage(body)
        it.setPositiveButton("Ok") { _, _ ->
            callback()
        }
    }.create().show()
}

fun Context.showDeleteUserRequestDialog(
    title: String,
    body: String,
    callback: () -> Unit
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title)
        it.setMessage(body)
        it.setPositiveButton("Confirm") { _, _ ->
            callback()
        }
        it.setNegativeButton("Cancel") {_, _ ->
        }

    }.create().show()

}