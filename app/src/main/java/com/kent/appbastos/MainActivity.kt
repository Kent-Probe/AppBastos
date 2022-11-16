package com.kent.appbastos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

const val APPBASTOS_EXTRA_CASHSALE = "com.kent.appbastos"

//const val CREDIT_SALE = "com.kent.appbastos.CreditSale"
const val OPERATIONAL_EXPENSES = "com.kent.appbastos.OperationalExpenses"
//Text of use

//const val NUMBER_CLIENT = "com.kent.appbastos.number"
const val TYPE_OPERATIONAL = "com.kent.appbastos.type"
const val DESCRIPTION = "com.kent.appbastos.description"
const val VALUE = "com.kent.appbastos.value"


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashThem)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn: Button = findViewById(R.id.btnLogin)
        btn.setOnClickListener {

            val editText: EditText = findViewById(R.id.txtName)
            val name = editText.text.toString()

            if (name.isNotEmpty()) {
                val intent: Intent = Intent(this, MainMenu::class.java).apply {
                    putExtra(APPBASTOS_EXTRA_CASHSALE, name)
                }
                startActivity(intent)
            } else {
                editText.error = "Error, el campo no puede estar vacio"
            }

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