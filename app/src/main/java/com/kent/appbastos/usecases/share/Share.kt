package com.kent.appbastos.usecases.share

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.kent.appbastos.R
import com.kent.appbastos.model.values.CashSaleClass
import java.io.File

class Share : AppCompatActivity() {

    private lateinit var privateRootDir: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)


        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString("profile", null).toString()
        val txtUserName: TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables Buttons
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnBackHome: Button = findViewById(R.id.btnBackHome)
        val btnShare: Button = findViewById(R.id.btnShare)
        //val btnPrintOut: Button = findViewById(R.id.btnPrintOut)

        btnBack.setOnClickListener {
            finish()
        }
        btnBackHome.setOnClickListener {
            finish()
        }

        btnShare.setOnClickListener {
            share()
        }
    }

    private fun share(){
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, intent.extras?.getString("text")) //Value to Share
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "Datos de la venta")
        }
        startActivity(Intent.createChooser(intent, null))
    }
}

