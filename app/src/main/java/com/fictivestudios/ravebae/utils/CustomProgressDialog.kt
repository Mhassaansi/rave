package com.fictivestudios.ravebae.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import com.fictivestudios.ravebae.R

class CustomProgressDialogue(context: Context?) : Dialog(
    context!!
) {
    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val view = LayoutInflater.from(context).inflate(
            R.layout.progress_dialog, null
        )

        setContentView(view)
    }
}