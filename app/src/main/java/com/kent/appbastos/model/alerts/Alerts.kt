package com.kent.appbastos.model.alerts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.kent.appbastos.R
import com.kent.appbastos.model.EventButtonsCallBack


class Alerts {

    fun showAlert(title:String, message:String, positiveButton:String, context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton, null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showAlertSelection(layoutInflater: LayoutInflater, context: Context, txtDialog: String, txtBtnUp: String, txtBtnDown: String, eventButtons: EventButtonsCallBack){
        val dialogAlertDialog = AlertDialog.Builder(context)
        val view: View = layoutInflater.inflate(R.layout.dialog_custom_selection, null)

        dialogAlertDialog.setView(view)

        val alertDialog = dialogAlertDialog.create()
        alertDialog.show()

        val btnUp: Button = view.findViewById(R.id.btnUp)
        btnUp.text = txtBtnUp
        val btnDown: Button = view.findViewById(R.id.btnDown)
        btnDown.text = txtBtnDown
        val textDialog: TextView = view.findViewById(R.id.textDialog)
        textDialog.text = txtDialog

        btnUp.setOnClickListener{
            eventButtons.buttonUp(alertDialog)
        }

        btnDown.setOnClickListener {
            eventButtons.buttonDown(alertDialog)
        }
    }


}

