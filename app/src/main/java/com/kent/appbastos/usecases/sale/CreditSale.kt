package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.usecases.remarks.AddRemarks
import com.kent.appbastos.model.validate.ValidateEmpty
import java.util.*




class CreditSale : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_sale)

        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString("profile", null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables buttons
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        //Click Buttons
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
                val view: View = Alerts().showAlertPersonalize(
                    layoutInflater,
                    this,
                    R.string.txtAlertDialog.toString(),
                    R.string.txtBtnOpenNewDebt.toString(),
                    R.string.txtBtnAddPayment.toString()
                )

                val btnUp: Button = view.findViewById(R.id.btnUp)
                val btnDown: Button = view.findViewById(R.id.btnDown)

                btnUp.setOnClickListener {
                    val intent = Intent(this, AddRemarks::class.java)
                    startActivity(intent)
                    finish()
                }
                btnDown.setOnClickListener {
                    val intent = Intent(this, Payment::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}