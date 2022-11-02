package com.kent.appbastos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OperationalExpenses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operational_expenses)

        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnContinue.setOnClickListener {
            val intent: Intent = Intent(this, AddRemarks::class.java)
            startActivity(intent)
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}