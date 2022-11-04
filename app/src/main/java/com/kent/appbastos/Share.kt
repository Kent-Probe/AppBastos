package com.kent.appbastos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Share : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnBackHome: Button = findViewById(R.id.btnBackHome)
        val btnShare: Button = findViewById(R.id.btnShare)
        val btnPrintOut: Button = findViewById(R.id.btnPrintOut)

        btnBack.setOnClickListener {
            finish()
        }
        btnBackHome.setOnClickListener {
            finish()
        }
    }
}

