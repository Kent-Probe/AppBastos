package com.kent.appbastos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

const val APPBASTOS_EXTRA_CASHSALE = "com.kent.appbastos.CashSale"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openScreen(view: View){
        val editText = findViewById<EditText>(R.id.txtName)
        val message = editText.text.toString()
        val intent = Intent(this, CashSale::class.java).apply {
            putExtra(APPBASTOS_EXTRA_CASHSALE, message)
        }
        startActivity(intent)
    }

}