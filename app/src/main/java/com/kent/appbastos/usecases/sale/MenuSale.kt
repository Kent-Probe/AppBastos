package com.kent.appbastos.usecases.sale

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kent.appbastos.R

class MenuSale : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_sale)

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