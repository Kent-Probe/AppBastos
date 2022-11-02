package com.kent.appbastos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AddRemarks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remarks)

        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnContinue.setOnClickListener {

        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}