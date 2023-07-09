package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.util.EventButtonsCallBack
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.validate.ValidateEmpty
import com.kent.appbastos.usecases.remarks.AddRemarks
import java.util.*


class CashSale : AppCompatActivity() {
    //Method create the layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_sale)

        //variable intent, data of the listInventory (Screen)
        val data :MutableMap<String, Any> = hashMapOf(
            Keys.KEY to intent.extras?.getString(Keys.KEY, Keys.ERROR).toString(),
            Keys.NAME to intent.extras?.getString(Keys.NAME, Keys.ERROR).toString(),
            Keys.PROVIDER to intent.extras?.getString(Keys.PROVIDER, Keys.ERROR).toString(),
            Keys.CATEGORY to intent.extras?.getString(Keys.CATEGORY, Keys.ERROR).toString(),
            Keys.VALUE_BASE to intent.extras?.getFloat(Keys.VALUE_BASE, 0f).toString().toFloat(),
            Keys.AMOUNT to intent.extras?.getFloat(Keys.AMOUNT, 0f).toString().toFloat(),
            Keys.AMOUNT_MIN to intent.extras?.getFloat(Keys.AMOUNT_MIN, 0f).toString().toFloat()
        )

        //Value of format and more string with format
        val valueBase = data[Keys.VALUE_BASE].toString().toFloat()
        var isRepeated: Boolean
        var valueRepeated = -100f

        //Variables of screen (textView) with default data
        val type:TextView = findViewById(R.id.type)
        val category:TextView = findViewById(R.id.categoryProduct)
        val valueAmountView: TextInputEditText = findViewById(R.id.amount)
        val valueUnitView: TextInputEditText = findViewById(R.id.valueUnit)

        //Assign format the text edit
        valueUnitView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                valueUnitView.removeTextChangedListener(this)
                val cleanString = s.toString().replace("[$.,COP\\s]".toRegex(), "")
                val textFormat = formatTextPrice(cleanString)
                valueUnitView.setText(textFormat)
                valueUnitView.setSelection(textFormat.length - 4)
                valueUnitView.addTextChangedListener(this)
            }
        })
        valueAmountView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                valueAmountView.removeTextChangedListener(this)
                val cleanString = s.toString().replace("[$.,und\\s]".toRegex(), "")
                val textFormat = formatTextAmount(cleanString)
                valueAmountView.setText(textFormat)
                valueAmountView.setSelection(textFormat.length - 4)
                valueAmountView.addTextChangedListener(this)
            }

        })


        //assign default values (variables of the screen)
        type.text = data[Keys.NAME].toString()
        category.text = data[Keys.CATEGORY].toString()

        //disable textView
        type.isEnabled = false
        category.isEnabled = false

        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString(Keys.PROFILE, null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables of buttons
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)
        val btnBack: ImageButton = findViewById(R.id.btnBack)

        //Click buttons
        btnContinue.setOnClickListener {

            //Variables de los TextInputLayout
            val inputValueUnit: TextInputLayout = findViewById(R.id.inputValueUnit)
            val inputValueAmount: TextInputLayout = findViewById(R.id.inputAmount)

            //Variables del  editText
            val nameClientView: TextView = findViewById(R.id.nameClient)

            //Variables de los text del TextInputLayout
            var nameClient: String = nameClientView.text.toString()
            val valueUnit: String = valueUnitView.text.toString().replace("[$.,COP\\s]".toRegex(), "")
            val valueAmount: String = valueAmountView.text.toString().replace("[$.,und\\s]".toRegex(), "")

            //Arrays
            val texts = Vector<String>()
            val inputsLayouts = Vector<TextInputLayout>()

            //Fill arrays
            texts.addAll(
                listOf(
                    valueUnit,
                    valueAmount
                )
            )
            inputsLayouts.addAll(
                listOf(
                    inputValueUnit,
                    inputValueAmount
                )
            )

            //SHow mistake
            if (ValidateEmpty().validate(texts, inputsLayouts) && ValidateEmpty().valueUnit(valueUnit, inputValueUnit)) {
                if(nameClient.isEmpty()){
                    nameClient = "temporal"
                }

                //Validate data of the screen with the Inventory
                if (valueAmount.isNotEmpty() && data[Keys.AMOUNT].toString().toFloat() < valueAmount.toFloat()) {
                    inputValueAmount.isErrorEnabled = true
                    inputValueAmount.error = Keys.ERROR_WITHOUT_INVENTORY
                    return@setOnClickListener
                }else{
                    inputValueAmount.isErrorEnabled = false
                }

                isRepeated = valueRepeated != valueUnit.toFloatOrNull()
                var continueScreen = true

                if(valueUnit.isNotEmpty() && valueBase > valueUnit.toFloat() && isRepeated) {
                    continueScreen = false
                    Alerts().showAlertSelection(
                        layoutInflater,
                        this,
                        String.format(Keys.ALERT_MSM_VALUE_MIN, Keys.FORMAT_PRICE.format(valueBase)),
                        Keys.BUTTON_CONTINUE,
                        Keys.BUTTON_CHANGE,
                        object : EventButtonsCallBack {
                            override fun buttonUp(alertDialog: AlertDialog) {
                                valueRepeated = valueUnit.toFloat()
                                isRepeated = false
                                continueScreen = true
                                alertDialog.hide()
                            }

                            override fun buttonDown(alertDialog: AlertDialog) {
                                valueUnitView.setText("")
                                alertDialog.hide()
                            }

                        }
                    )
                }else{
                    valueRepeated = -100f
                }


                val amountInventory = data[Keys.AMOUNT].toString().toFloat() - valueAmount.toFloat()

                if(continueScreen){
                    val intent = Intent(this, AddRemarks::class.java).apply {
                        putExtra(Keys.NAME_CLIENT, nameClient)
                        putExtra(Keys.CATEGORY, data[Keys.CATEGORY].toString())
                        putExtra(Keys.VALUE_UNIT, valueUnit.toFloat())
                        putExtra(Keys.VALUE_AMOUNT, valueAmount.toInt())
                        putExtra(Keys.TYPE, data[Keys.NAME].toString())
                        putExtra(Keys.TITLE, Keys.CASH_SALE)
                        putExtra(Keys.KEY_INVENTORY, data[Keys.KEY].toString())
                        putExtra(Keys.PROVIDER, data[Keys.PROVIDER].toString())
                        putExtra(Keys.FLETE, data[Keys.FLETE].toString())
                        putExtra(Keys.AMOUNT_INVENTORY, amountInventory)
                    }
                    startActivity(intent)
                    finish()
                }

            }

        }

        btnBack.setOnClickListener{
            finish()
        }
        btnCancel.setOnClickListener {
            finish()
        }

    }

    private fun formatTextPrice(text: String): String {
        val float = text.toFloatOrNull() ?: 0f
        return Keys.FORMAT_PRICE.format(float)
    }

    private fun formatTextAmount(text: String): String {
        val float = text.toFloatOrNull() ?: 0f
        return Keys.FORMAT_AMOUNT.format(float)
    }
}