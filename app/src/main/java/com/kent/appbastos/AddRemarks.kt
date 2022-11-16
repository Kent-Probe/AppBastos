package com.kent.appbastos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class AddRemarks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remarks)

        //Buttons
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        //Text of topics
        //val cashSale = this.intent.extras?.getString(CASH_SALE)


        btnContinue.setOnClickListener {
            val intent = Intent(this, Share::class.java)
            startActivity(intent)
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
        btnCancel.setOnClickListener {
            finish()
        }
    }

}