package com.kent.appbastos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.validate.ValidateEmpty
import java.util.*

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnContinue.setOnClickListener {

            //Variables de los TextInputLayout
            val inputNameClient: TextInputLayout = findViewById(R.id.inputNameClient)
            val inputDebt: TextInputLayout = findViewById(R.id.inputDebt)
            val inputNameProduct: TextInputLayout = findViewById(R.id.inputNameProduct)
            val inputPayment: TextInputLayout = findViewById(R.id.inputPayment)


            //Variables del  editText
            val nameClientView: TextView = findViewById(R.id.nameClient)
            val debtView: TextView = findViewById(R.id.debt)
            val nameProductView: TextView = findViewById(R.id.nameProduct)
            val paymentView: TextView = findViewById(R.id.payment)


            //Variables de los text del TextInputLayout
            val nameClient: String = nameClientView.text.toString()
            val debt: String = debtView.text.toString()
            val nameProduct: String = nameProductView.text.toString()
            val payment: String = paymentView.text.toString()

            //Arrays
            val texts: Vector<String> = Vector(listOf(nameClient, debt, nameProduct, payment))
            val inputsLayouts: Vector<TextInputLayout> =
                Vector(listOf(inputNameClient, inputDebt, inputNameProduct, inputPayment))

            if (ValidateEmpty().validate(texts, inputsLayouts)) {
                val intent = Intent(this, AddRemarks::class.java)
                startActivity(intent)
                finish()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}