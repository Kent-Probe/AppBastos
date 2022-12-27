package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.kent.appbastos.R

class MenuSale : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_sale)

        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString("profile", null).toString()
        val txtUserName: TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables Buttons
        val btnCashSale: Button = findViewById(R.id.btnCashSale)
        val btnCreditSale: Button = findViewById(R.id.btnCreditSale)

        btnCashSale.setOnClickListener {
            val intent = Intent(this, CashSale::class.java)
            startActivity(intent)
        }

        btnCreditSale.setOnClickListener {
            val intent = Intent(this, CreditSale::class.java)
            startActivity(intent)
        }
    }
}