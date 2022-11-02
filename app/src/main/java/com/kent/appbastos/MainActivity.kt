package com.kent.appbastos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

const val APPBASTOS_EXTRA_CASHSALE = "com.kent.appbastos.CashSale"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashThem)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn: Button = findViewById(R.id.btnLogin)
        btn.setOnClickListener{
            val intent: Intent = Intent(this, MainMenu:: class.java)
            startActivity(intent)
        }

    }


/*
    fun openScreen(view: View){
        val editText = findViewById<EditText>(R.id.txtName)
        val message = editText.text.toString()
        val intent = Intent(this, CashSale::class.java).apply {
            putExtra(APPBASTOS_EXTRA_CASHSALE, message)
        }
        startActivity(intent)
    }
    */

}