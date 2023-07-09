package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.validate.ValidateEmpty
import com.kent.appbastos.usecases.remarks.AddRemarks
import java.util.*

class OperationalExpenses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operational_expenses)

        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString(Keys.PROFILE, null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables of buttons
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)
        findViewById<ImageButton?>(R.id.btnBack).setOnClickListener {
            finish()
        }

        //Click buttons
        btnContinue.setOnClickListener {

            //Variables de los TextInputLayout
            val inputTypeOperational: TextInputLayout = findViewById(R.id.InputTypeOperational)
            val inputDescription: TextInputLayout = findViewById(R.id.InputDescription)
            val inputValue: TextInputLayout = findViewById(R.id.InputValue)

            //Variables del  editText
            val typeOperationalView: TextView = findViewById(R.id.typeOperational)
            val descriptionView: TextView = findViewById(R.id.description)
            val valueView: TextView = findViewById(R.id.value)

            //Variables de los text del TextInputLayout
            val typeOperational: String = typeOperationalView.text.toString()
            val description: String = descriptionView.text.toString()
            val value: String = valueView.text.toString()

            //Arrays
            val texts: Vector<String> = Vector(
                listOf(
                    typeOperational,
                    description,
                    value
                )
            )
            val inputsLayouts: Vector<TextInputLayout> = Vector(
                listOf(
                    inputTypeOperational,
                    inputDescription,
                    inputValue
                )
            )

            if (ValidateEmpty().validate(texts, inputsLayouts)) {
                val intent = Intent(this, AddRemarks::class.java)
                startActivity(intent)
                finish()
            }

        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}