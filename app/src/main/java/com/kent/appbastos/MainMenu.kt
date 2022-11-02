package com.kent.appbastos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btn:Button = findViewById(R.id.btnCashSale)
        btn.setOnClickListener {
            val intent: Intent = Intent(this, CashSale:: class.java)
            startActivity(intent)
        }
    }
}