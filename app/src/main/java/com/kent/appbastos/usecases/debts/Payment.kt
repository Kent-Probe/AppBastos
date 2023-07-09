package com.kent.appbastos.usecases.debts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.usecases.users.ListUsers

class Payment : AppCompatActivity() {

    private lateinit var btnSeeUser: Button
    private lateinit var btnSeeDebts: Button
    private lateinit var key: String
    private var valueTotal: Float = 0f

    //Response of the activity
    private val responseLauncherUsers = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            btnSeeUser.text = it.data?.getStringExtra(Keys.USERNAME)
            btnSeeDebts.visibility = View.VISIBLE
        }
    }

    private val responseLauncherDebts = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            btnSeeDebts.text = Keys.FORMAT_PRICE.format(it.data?.getFloatExtra(Keys.VALUE_TOTAL, 0f))
            key = it.data?.getStringExtra(Keys.KEY_DEBTS).toString()
            valueTotal = (btnSeeDebts.text as String).replace("[$.,COP\\s]".toRegex(), "").toFloat()
        }
    }

    override fun onStart() {
        super.onStart()
        btnSeeUser.error = null
        btnSeeUser.error = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString(Keys.PROFILE, null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables Buttons
        val btnCancel: Button = findViewById(R.id.btnCancel)
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnBack: ImageButton = findViewById(R.id.btnBack)

        //Variables
        val inputPayment: TextInputLayout = findViewById(R.id.inputPayment)
        val payment: TextInputEditText = findViewById(R.id.payment)

        //Assign format the text edit
        payment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                payment.removeTextChangedListener(this)
                val cleanString = s.toString().replace("[$.,COP\\s]".toRegex(), "")
                val textFormat = formatTextPrice(cleanString)
                payment.setText(textFormat)
                payment.setSelection(textFormat.length - 4)
                payment.addTextChangedListener(this)
            }
        })

        //assign button values
        btnSeeDebts = findViewById(R.id.btnDebt)
        btnSeeUser = findViewById(R.id.btnNameUserAdd)
        btnSeeDebts.text = Keys.DEBTS_ES
        btnSeeUser.setOnClickListener {
            val intent = Intent(this, ListUsers::class.java).apply {
                putExtra(Keys.CREDIT_SALE, true)
            }
            responseLauncherUsers.launch(intent)

        }

        btnSeeDebts.setOnClickListener {
            val intent = Intent(this, ListDebts::class.java).apply {
                putExtra(Keys.KEY, btnSeeUser.text)
            }
            responseLauncherDebts.launch(intent)
            btnSeeDebts.text = Keys.DEBTS_ES
            //btnSeeDebts.visibility = View.GONE
        }

        btnCancel.setOnClickListener {
            finish()
        }
        btnBack.setOnClickListener {
            finish()
        }

        btnContinue.setOnClickListener {
            val paymentNumber = payment.text.toString().replace("[$.,COP\\s]".toRegex(), "").toFloatOrNull() ?: 0f
            if(btnSeeDebts.visibility == View.VISIBLE){
                btnSeeUser.error = null
                if (btnSeeDebts.text.toString().uppercase() == Keys.DEBTS_ES.uppercase()) {
                    btnSeeDebts.error = "Debe seleccionar una deuda"
                    return@setOnClickListener
                }
                btnSeeDebts.error = null
                if (payment.text.isNullOrEmpty() || key.isEmpty()) {
                    inputPayment.isErrorEnabled = true
                    inputPayment.error = Keys.ERROR_FIELD_EMPTY
                } else if (paymentNumber > valueTotal) {
                    inputPayment.isErrorEnabled = true
                    inputPayment.error = Keys.ERROR_DEBTS_GREATER
                } else {
                    inputPayment.isErrorEnabled = false
                    val totalCurrency = valueTotal - paymentNumber
                    DataBaseShareData().addPayment(btnSeeUser.text.toString(), key, totalCurrency)
                    Toast.makeText(this, Keys.TOAST_ADD_PAYMENT, Toast.LENGTH_LONG).show()
                    finish()
                }
            }else{
                btnSeeUser.error = "Debe seleccionar un cliente"
            }

        }
    }

    private fun formatTextPrice(text: String): String {
        val float = text.toFloatOrNull() ?: 0f
        return Keys.FORMAT_PRICE.format(float)
    }
}