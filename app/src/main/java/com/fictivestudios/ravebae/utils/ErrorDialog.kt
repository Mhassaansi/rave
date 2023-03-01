package com.fictivestudios.ravebae.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import com.fictivestudios.ravebae.R

//object ErrorDialog {
//    fun showErrorDialog(context: Context, msg: String?) {
//        val dialogBuilder = AlertDialog.Builder(context)
//        val inflater = (context as Activity).layoutInflater
//        val dialogView = inflater.inflate(R.layout.confirm_dialog, null)
//        dialogBuilder.setView(dialogView)
//        val icon = dialogView.findViewById<ImageView>(R.id.icon)
//        val title: AppCompatTextView = dialogView.findViewById(R.id.title)
//        title.text = msg
//        val btnNag: AppCompatTextView = dialogView.findViewById(R.id.btnNag)
//        val btnPos: AppCompatTextView = dialogView.findViewById(R.id.btnPos)
//        btnPos.visibility = View.GONE
//        val alertDialog = dialogBuilder.create()
//        alertDialog.show()
//        btnNag.setOnClickListener { alertDialog.dismiss() }
//        btnPos.setOnClickListener { alertDialog.dismiss() }
//    }
//}