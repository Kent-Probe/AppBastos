package com.kent.appbastos.usecases.receipt

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.util.Keys

class ReceiptOpenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receipt_open)

        //Change name user
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString(Keys.PROFILE, null).toString()
        val txtUserName: TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //val's
        val amountReceipt = intent?.extras?.getString(Keys.INVENTORY)
        val keyInventory = intent?.extras?.getString(Keys.KEY_INVENTORY)

        //val's regex
        val regex = "[\$.,/und\\s]".toRegex()

        val amountNumber = amountReceipt?.replace(regex, "")

        //Change title
        findViewById<TextView>(R.id.txtNameScreen).text = Keys.DEBTS_ES

        //text input
        val amount = findViewById<TextInputEditText>(R.id.amountInventory)
        val inputAMount = findViewById<TextInputLayout>(R.id.inputPayment)

        amount.addTextChangedListener {
            if(it.toString().toFloat() > amountNumber?.toFloat()!!){
                amount.setText(amountNumber.toString())
            }
        }

        //button
        findViewById<Button>(R.id.btnApply).setOnClickListener {
            if(amount.text.isNullOrEmpty()){
                inputAMount.isErrorEnabled = true
                inputAMount.error = "No puede estar vacio"
                return@setOnClickListener
            }
            inputAMount.isErrorEnabled = false
            DataBaseShareData().writeNewAmountInventory(amountNumber?.toFloat(), keyInventory)
            Toast.makeText(this, "Exito al anular factura", Toast.LENGTH_LONG).show()
            finish()
        }
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
