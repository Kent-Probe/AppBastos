package com.kent.appbastos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.validate.ValidateEmpty
import java.util.*


class CashSale : AppCompatActivity() {
    //Metodo Crea el layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_sale)

        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnContinue.setOnClickListener {

            //Variables de los TextInputLayout
            val inputNameClient: TextInputLayout = findViewById(R.id.inputNameClient)
            val inputNameProduct: TextInputLayout = findViewById(R.id.inputNameProduct)
            val inputValueUnit: TextInputLayout = findViewById(R.id.inputValueUnit)
            val inputValueAmount: TextInputLayout = findViewById(R.id.inputAmount)

            //Variables del  editText
            val nameClientView: TextView = findViewById(R.id.nameClient)
            val nameProductView: TextView = findViewById(R.id.nameProduct)
            val valueUnitView: TextView = findViewById(R.id.valueUnit)
            val valueAmountView: TextView = findViewById(R.id.amount)

            //Variables de los text del TextInputLayout
            val nameClient: String = nameClientView.text.toString()
            val nameProduct: String = nameProductView.text.toString()
            val valueUnit: String = valueUnitView.text.toString()
            val valueAmount: String = valueAmountView.text.toString()

            //Arrays
            val texts = Vector<String>()
            val inputsLayouts = Vector<TextInputLayout>()

            //Llenar arrays
            texts.addAll(listOf(nameClient, nameProduct, valueUnit, valueAmount))
            inputsLayouts.addAll(
                listOf(
                    inputNameClient,
                    inputNameProduct,
                    inputValueUnit,
                    inputValueAmount
                )
            )

            if (ValidateEmpty().validate(texts, inputsLayouts)) {
                val intent = Intent(this, AddRemarks::class.java)
                startActivity(intent)
                finish()
            }

        }

        btnCancel.setOnClickListener {
            finish()
        }


        /*
        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(APPBASTOS_EXTRA_CASHSALE)

        //Capture the layout's TexView and set the string as its text
        val textView = findViewById<TextView>(R.id.txtUserName).apply {
            text = message
        }

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