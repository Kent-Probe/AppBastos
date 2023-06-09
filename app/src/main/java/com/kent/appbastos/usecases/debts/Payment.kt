package com.kent.appbastos.usecases.debts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.usecases.users.ListUsers

class Payment : AppCompatActivity() {

    private lateinit var btnSeeUser: Button
    private lateinit var btnSeeDebts: Button
    private lateinit var key: String
    private var valueTotal: Float = 0f

    //Response of the activity
    private val responseLauncherUsers = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            btnSeeUser.text = it.data?.getStringExtra(ListUsers.USERNAME)
            btnSeeDebts.visibility = View.VISIBLE
        }
    }

    private val responseLauncherDebts = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            btnSeeDebts.text = it.data?.getFloatExtra(ListDebts.VALUE_TOTAL, 0f).toString()
            key = it.data?.getStringExtra(ListDebts.KEY_DEBTS).toString()
            valueTotal = (btnSeeDebts.text as String).toFloat()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString("profile", null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables Buttons
        val btnCancel: Button = findViewById(R.id.btnCancel)
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnBack: ImageButton = findViewById(R.id.btnBack)

        //assign button values
        btnSeeDebts = findViewById(R.id.btnDebt)
        btnSeeUser = findViewById(R.id.btnNameUserAdd)
        btnSeeUser.setOnClickListener {
            val intent = Intent(this, ListUsers::class.java).apply {
                putExtra("creditSale", true)
            }
            responseLauncherUsers.launch(intent)

        }

        btnSeeDebts.setOnClickListener {
            val intent = Intent(this, ListDebts::class.java).apply {
                putExtra("key", btnSeeUser.text)
            }
            responseLauncherDebts.launch(intent)
            val temp = "deuda"
            btnSeeDebts.text = temp
            //btnSeeDebts.visibility = View.GONE
        }

        btnCancel.setOnClickListener {
            finish()
        }
        btnBack.setOnClickListener {
            finish()
        }

        btnContinue.setOnClickListener {

            val inputPayment: TextInputLayout = findViewById(R.id.inputPayment)
            val payment: TextView = findViewById(R.id.payment)

            if(btnSeeDebts.visibility == View.VISIBLE){
                if(payment.text.isEmpty() || key.isEmpty()){
                    inputPayment.isErrorEnabled = true
                    inputPayment.error = "Error: Campo Vacio"
                }
                else if (payment.text.toString().toFloat() > valueTotal){
                    inputPayment.isErrorEnabled = true
                    inputPayment.error = "Error: Valor mayor a la deuda registrada"
                }
                else{
                    inputPayment.isErrorEnabled = false
                    DataBaseShareData().addPayment(btnSeeUser.text.toString(), key, payment.text.toString().toFloat())
                    Toast.makeText(this, "Se agrego el pago", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

        }
    }
}