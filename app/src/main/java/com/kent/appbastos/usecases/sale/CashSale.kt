package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.usecases.remarks.AddRemarks
import com.kent.appbastos.model.validate.ValidateEmpty
import java.util.*


class CashSale : AppCompatActivity() {
    //Method create the layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_sale)

        //Spinner
        val type: Spinner = findViewById(R.id.type)

        //Options
        val options = arrayOf("v", "m")

        //Adapter
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options)

        //Adapter with the spinner
        type.adapter = adapter
        type.setSelection(0)

        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString("profile", null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables of buttons
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)
        val btnBack: ImageButton = findViewById(R.id.btnBack)

        //Click buttons
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
            texts.addAll(
                listOf(
                    nameClient,
                    nameProduct,
                    valueUnit,
                    valueAmount
                )
            )
            inputsLayouts.addAll(
                listOf(
                    inputNameClient,
                    inputNameProduct,
                    inputValueUnit,
                    inputValueAmount
                )
            )

            //SHow mistake
            if (ValidateEmpty().validate(texts, inputsLayouts) && ValidateEmpty().valueUnit(valueUnit, inputValueUnit)) {
                val intent = Intent(this, AddRemarks::class.java).apply {
                    putExtra("nameClient", nameClient)
                    putExtra("nameProduct", nameProduct)
                    putExtra("valueUnit", valueUnit.toFloat())
                    putExtra("valueAmount", valueAmount.toInt())
                    putExtra("type", type.selectedItem.toString())
                    putExtra("title", "CashSale")
                }

                startActivity(intent)
                finish()
            }

        }

        btnBack.setOnClickListener{
            finish()
        }
        btnCancel.setOnClickListener {
            finish()
        }

    }
}