package com.kent.appbastos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.kent.appbastos.validate.ValidateEmpty


class AddRemarks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remarks)

        //Buttons
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        //Texts of field
        val txtRemarks: TextView = findViewById(R.id.txtRemarks)

        //Text of topics
        val values = this.intent.extras?.getString(VALUES_SAVE)?.split(".")
        var text = this.intent.extras?.getString(VALUES_SAVE)

        if (!ValidateEmpty().validateEmptyOrNull(values)) {
            //----------------------------------------------------------Mostrar mensaje de error
            finish()
        }

        btnContinue.setOnClickListener {
            if(txtRemarks.text.isNotEmpty()){
                text += ".$txtRemarks"
            }
            val intent = Intent(this, Share::class.java).apply {
                putExtra(VALUES_SAVE, text)
            }
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