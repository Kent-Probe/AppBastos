package com.kent.appbastos.model.alerts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.kent.appbastos.R
import com.kent.appbastos.model.util.BtnFractions
import com.kent.appbastos.model.util.EventButtonsCallBack



class Alerts {

    fun showAlert(title:String, message:String, positiveButton:String, context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton, null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showAlertDateForMont(layoutInflater: LayoutInflater, context: Context){
        val view: View = layoutInflater.inflate(R.layout.dialog_date_year_mont, null)
        val dialogAlert = AlertDialog.Builder(context)
        dialogAlert.setView(view)
        val dialog = dialogAlert.create()
        dialog.show()

        val pickerNumber = view.findViewById<NumberPicker>(R.id.pickerNumber)
        pickerNumber.minValue = 2010
        pickerNumber.maxValue = 2100
        pickerNumber.value = 2022
        pickerNumber.setFormatter { String.format("%02d", it) }
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

    fun showAlertSelection(layoutInflater: LayoutInflater, context: Context, txtDialog: String, txtBtn: String, btnFractions: BtnFractions){
        val dialogAlert = AlertDialog.Builder(context)
        val view: View = layoutInflater.inflate(R.layout.dialog_custom, null)

        dialogAlert.setView(view)

        val alertDialog = dialogAlert.create()
        alertDialog.show()

        //txt Title and text button
        view.findViewById<TextView>(R.id.textDialog).text = txtDialog
        view.findViewById<Button>(R.id.btnCancel).text = txtBtn

        //Buttons
        view.findViewById<Button>(R.id.btn1).setOnClickListener { btnFractions.btn1(alertDialog) }

        view.findViewById<Button>(R.id.btn2).setOnClickListener { btnFractions.btn2(alertDialog) }

        view.findViewById<Button>(R.id.btn3).setOnClickListener { btnFractions.btn3(alertDialog) }

        view.findViewById<Button>(R.id.btn4).setOnClickListener { btnFractions.btn4(alertDialog) }

        view.findViewById<Button>(R.id.btnCancel).setOnClickListener { btnFractions.btnCancel(alertDialog) }

    }


}

