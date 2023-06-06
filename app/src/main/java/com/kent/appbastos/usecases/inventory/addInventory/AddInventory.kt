package com.kent.appbastos.usecases.inventory.addInventory

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.validate.ValidateEmpty
import java.util.*

class AddInventory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inventary)

        //Spinner
        val type: Spinner = findViewById(R.id.type)

        //Options
        val options = arrayOf("v", "m")

        //Adapter
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options)

        //Adapter with the spinner
        type.adapter = adapter
        type.setSelection(0)

        //Buttons
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)


        btnContinue.setOnClickListener {
            val amountView: TextView = findViewById(R.id.amount)
            val valueView: TextView = findViewById(R.id.value)
            val providerView: TextView = findViewById(R.id.provider)
            val amountMinView: TextView = findViewById(R.id.amountMin)
            val fleteView: TextView = findViewById(R.id.flete)

            val inputAmount: TextInputLayout = findViewById(R.id.inputAmount)
            val inputValue: TextInputLayout = findViewById(R.id.inputValue)
            val inputProvider: TextInputLayout = findViewById(R.id.inputProvider)
            val inputAmountMin: TextInputLayout = findViewById(R.id.inputAmountMin)
            val inputFlete: TextInputLayout = findViewById(R.id.inputFlete)

            val textAmount = amountView.text.toString()
            val textValue = valueView.text.toString()
            val textProvider = providerView.text.toString()
            val textAmountMin = amountMinView.text.toString()
            val textFlete = fleteView.text.toString()

            //Arrays
            val texts: Vector<String> = Vector(
                listOf(
                    textAmount,
                    textValue,
                    textProvider,
                    textAmountMin,
                    textFlete
                )
            )

            val inputLayouts: Vector<TextInputLayout> = Vector(
                listOf(
                    inputAmount,
                    inputValue,
                    inputProvider,
                    inputAmountMin,
                    inputFlete
                )
            )

            if(!ValidateEmpty().validate(texts, inputLayouts)) {
                Toast.makeText(this, "Algun dato no es valido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!ValidateEmpty().validateMin(textAmount, textAmountMin, inputAmount, inputAmountMin) ){
                Toast.makeText(this, "Dato no valido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            texts.add(type.selectedItem.toString())

            DataBaseShareData().addInventory(texts, this)
            finish()

        }
        btnCancel.setOnClickListener {
            finish()
        }

    }
}