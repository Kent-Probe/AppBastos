package com.kent.appbastos.usecases.inventory.addInventory

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.validate.ValidateEmpty
import java.util.*

class AddInventory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inventary)

        //Change name user
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString(Keys.PROFILE, null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

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
        findViewById<ImageButton?>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnContinue.setOnClickListener {
            val amountView: TextView = findViewById(R.id.amount)
            val valueView: TextView = findViewById(R.id.value)
            val providerView: TextView = findViewById(R.id.provider)
            val amountMinView: TextView = findViewById(R.id.amountMin)
            val fleteView: TextView = findViewById(R.id.flete)
            val categoryView: TextView = findViewById(R.id.categoryProduct)

            val inputAmount: TextInputLayout = findViewById(R.id.inputAmount)
            val inputValue: TextInputLayout = findViewById(R.id.inputValue)
            val inputProvider: TextInputLayout = findViewById(R.id.inputProvider)
            val inputAmountMin: TextInputLayout = findViewById(R.id.inputAmountMin)
            val inputFlete: TextInputLayout = findViewById(R.id.inputFlete)
            val inputCategory: TextInputLayout = findViewById(R.id.inputCategoryProduct)

            val textAmount = amountView.text.toString()
            val textValue = valueView.text.toString()
            val textProvider = providerView.text.toString()
            val textAmountMin = amountMinView.text.toString()
            val textFlete = fleteView.text.toString()
            val txtCategory = categoryView.text.toString()

            //Arrays
            val texts: Vector<String> = Vector(
                listOf(
                    textAmount,
                    textValue,
                    textProvider,
                    textAmountMin,
                    textFlete,
                    txtCategory
                )
            )

            val inputLayouts: Vector<TextInputLayout> = Vector(
                listOf(
                    inputAmount,
                    inputValue,
                    inputProvider,
                    inputAmountMin,
                    inputFlete,
                    inputCategory
                )
            )

            if(!ValidateEmpty().validate(texts, inputLayouts)) {
                Toast.makeText(this, Keys.TOAST_SOME_DATA_NOT_VALID, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!ValidateEmpty().validateMin(textAmount, textAmountMin, inputAmount, inputAmountMin) ){
                Toast.makeText(this, Keys.TOAST_DATA_NOT_VALID, Toast.LENGTH_LONG).show()
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