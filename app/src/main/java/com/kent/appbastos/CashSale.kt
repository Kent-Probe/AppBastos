package com.kent.appbastos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CashSale : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cash_sale)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(APPBASTOS_EXTRA_CASHSALE)

        //Capture the layout's TexView and set the string as its text
        val textView = findViewById<TextView>(R.id.txtUserName).apply {
            text = message
        }

        /*
        val spTypesValues : Spinner =  findViewById(R.id.spnType)

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.types,
            R.layout.spinner_item_types
        ).also {  arrayAdapter ->
            //Specify the layout to use when the list of choices appears
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //Apply the adapter to the spinner
            spTypesValues.adapter = arrayAdapter
        }
         */

    }
}