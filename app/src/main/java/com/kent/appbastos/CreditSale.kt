package com.kent.appbastos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.validate.ValidateEmpty
import java.util.*




class CreditSale : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_sale)


        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnContinue.setOnClickListener {

            //Variables de los TextInputLayout
            val inputNameClient: TextInputLayout = findViewById(R.id.inputNameClient)
            val inputNumberClient: TextInputLayout = findViewById(R.id.inputNumberClient)
            val inputNameProduct: TextInputLayout = findViewById(R.id.inputNameProduct)
            val inputValueUnit: TextInputLayout = findViewById(R.id.inputValueUnit)
            val inputValueAmount: TextInputLayout = findViewById(R.id.inputAmount)

            //Variables del  editText
            val nameClientView: TextView = findViewById(R.id.nameClient)
            val numberClientView: TextView = findViewById(R.id.numberClient)
            val nameProductView: TextView = findViewById(R.id.nameProduct)
            val valueUnitView: TextView = findViewById(R.id.valueUnit)
            val valueAmountView: TextView = findViewById(R.id.amount)

            //Variables de los text del TextInputLayout
            val nameClient: String = nameClientView.text.toString()
            val numberClient: String = numberClientView.text.toString()
            val nameProduct: String = nameProductView.text.toString()
            val valueUnit: String = valueUnitView.text.toString()
            val valueAmount: String = valueAmountView.text.toString()

            //Arrays
            val texts: Vector<String> = Vector(
                listOf(
                    nameClient,
                    numberClient,
                    nameProduct,
                    valueUnit,
                    valueAmount
                )
            )
            val inputsLayouts: Vector<TextInputLayout> = Vector(
                listOf(
                    inputNameClient,
                    inputNumberClient,
                    inputNameProduct,
                    inputValueUnit,
                    inputValueAmount
                )
            )


            if (ValidateEmpty().validate(texts, inputsLayouts)) {

                val dialogAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
                val layoutInflater: LayoutInflater = layoutInflater
                val view: View = layoutInflater.inflate(R.layout.dialog_custom, null)

                dialogAlertDialog.setView(view)
                val alertDialog: AlertDialog = dialogAlertDialog.create()

                val btnOpenAddDebt: Button = view.findViewById(R.id.btnOpenNewDebt)
                val btnAddPayment: Button = view.findViewById(R.id.btnAddPayment)

                btnOpenAddDebt.setOnClickListener {
                    val intent = Intent(this, AddRemarks::class.java).apply {
                    }
                    startActivity(intent)
                    finish()
                }
                btnAddPayment.setOnClickListener {
                    val intent = Intent(this, Payment::class.java)
                    startActivity(intent)
                    finish()
                }

                alertDialog.show()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}