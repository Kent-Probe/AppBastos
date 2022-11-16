package com.kent.appbastos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.validate.ValidateEmpty
import java.util.*

class OperationalExpenses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operational_expenses)

        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

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
                val intent = Intent(this, AddRemarks::class.java).apply {
                    putExtra(TYPE_OPERATIONAL, typeOperational)
                    putExtra(DESCRIPTION, description)
                    putExtra(VALUE, value)
                    putExtra(OPERATIONAL_EXPENSES, "operationalExpenses")
                }
                startActivity(intent)
                finish()
            }

        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}