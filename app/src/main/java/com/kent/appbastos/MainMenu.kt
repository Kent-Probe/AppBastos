package com.kent.appbastos

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnCashSale: Button = findViewById(R.id.btnCashSale)
        val btnCreditSale: Button = findViewById(R.id.txtCreditSale)
        val btnOperationalExpenses: Button = findViewById(R.id.btnOperationalExpenses)

        btnCashSale.setOnClickListener {
            val intent = Intent(this, CashSale::class.java)
            startActivity(intent)
        }

        btnCreditSale.setOnClickListener {
            val intent = Intent(this, CreditSale::class.java)
            startActivity(intent)
        }

        btnOperationalExpenses.setOnClickListener {
            val intent = Intent(this, OperationalExpenses::class.java)
            startActivity(intent)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if (keyCode == event.keyCode) run {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("¿Desea salir?")
                    .setPositiveButton("si", DialogInterface.OnClickListener { dialog, which ->
                        finishAffinity()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                alertDialog.show()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}