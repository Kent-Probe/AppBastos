package com.kent.appbastos.model.alerts

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.kent.appbastos.R


class Alerts {

    fun showAlert(title:String, message:String, positiveButton:String, context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton, null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showAlertPersonalize(layoutInflater: LayoutInflater, context: Context, txtDialog: String, txtBtnUp: String, txtBtnDown: String):View {
        val dialogAlertDialog: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(context)
        val view: View = layoutInflater.inflate(R.layout.dialog_custom, null)

        val btnUp: Button = view.findViewById(R.id.btnUp)
        btnUp.text = txtBtnUp
        val btnDown: Button = view.findViewById(R.id.btnDown)
        btnDown.text = txtBtnDown
        val textDialog: TextView = view.findViewById(R.id.textDialog)
        textDialog.text = txtDialog

        dialogAlertDialog.setView(view)
        val alertDialog: androidx.appcompat.app.AlertDialog = dialogAlertDialog.create()

        alertDialog.show()

        return view
    }

}