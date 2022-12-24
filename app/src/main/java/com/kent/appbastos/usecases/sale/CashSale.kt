package com.kent.appbastos.usecases.sale

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.usecases.remarks.AddRemarks
import com.kent.appbastos.model.validate.ValidateEmpty
import java.util.*


class CashSale : AppCompatActivity() {
    //Metodo Crea el layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_sale)

        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)
        val btnBack: ImageButton = findViewById(R.id.btnBack)

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

            if (ValidateEmpty().validate(texts, inputsLayouts) && ValidateEmpty().valueUnit(valueUnit, inputValueUnit)) {
                val intent = Intent(this, AddRemarks::class.java)
                startActivity(intent)
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