package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kent.appbastos.R
import com.kent.appbastos.usecases.inventory.listInventory.ListInventory

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
        findViewById<ImageButton?>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnCashSale.setOnClickListener {
            val intent = Intent(this, CashSale::class.java)
            startActivity(intent)
        }

        btnCreditSale.setOnClickListener {
            val intent = Intent(this, ListInventory::class.java).apply {
                putExtra("creditSale", true)
            }
            startActivity(intent)
        }
    }
}