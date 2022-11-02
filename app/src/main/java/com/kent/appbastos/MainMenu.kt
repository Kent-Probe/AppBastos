package com.kent.appbastos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnCashSale: Button = findViewById(R.id.btnCashSale)
        val btnCreditSale: Button = findViewById(R.id.txtCreditSale)
        val btnOperationalExpenses: Button = findViewById(R.id.btnOperationalExpenses)

        btnCashSale.setOnClickListener {
            val intent: Intent = Intent(this, CashSale:: class.java)
            startActivity(intent)
        }

        btnCreditSale.setOnClickListener {
            val intent: Intent = Intent(this, CreditSale:: class.java)
            startActivity(intent)
        }

        btnOperationalExpenses.setOnClickListener {
            val intent: Intent = Intent(this, OperationalExpenses:: class.java)
            startActivity(intent)
        }
    }
}