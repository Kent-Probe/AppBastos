package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.util.EventButtonsCallBack
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.validate.ValidateEmpty
import com.kent.appbastos.usecases.receipt.Receipt
import com.kent.appbastos.usecases.users.clients.ListUsers
import java.util.*



class CreditSale : AppCompatActivity() {

    //Variables for the response Launcher (object that is the response of the activity)
    private lateinit var numberClientView: TextView
    private lateinit var inputNumberClient: TextInputLayout
    private lateinit var btnSeeUser: Button

    private lateinit var numberText: String

    //Response of the activity
    private val responseLauncher = registerForActivityResult(StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val phone = it.data?.getStringExtra(Keys.PHONE).toString()
            inputNumberClient.visibility = View.VISIBLE
            btnSeeUser.text = it.data?.getStringExtra(Keys.USERNAME)
            numberClientView.text = Keys.formatNumber(phone)
            numberText = phone

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_sale)

        //variable util
        val context: Context = this
        val layoutInflater = layoutInflater
        var amount = -100f

        //variable input text
        numberClientView = findViewById(R.id.numberClient)
        inputNumberClient = findViewById(R.id.inputNumberClient)

        //variable intent, data of the listInventory (Screen)
        val data :MutableMap<String, Any> = hashMapOf(
            Keys.KEY to intent.extras?.getString(Keys.KEY, Keys.ERROR).toString(),
            Keys.NAME to intent.extras?.getString(Keys.NAME, Keys.ERROR).toString(),
            Keys.PROVIDER to intent.extras?.getString(Keys.PROVIDER, Keys.ERROR).toString(),
            Keys.CATEGORY to intent.extras?.getString(Keys.CATEGORY, Keys.ERROR).toString(),
            Keys.VALUE_BASE to intent.extras?.getFloat(Keys.VALUE_BASE, 0f).toString().toFloat(),
            Keys.AMOUNT to intent.extras?.getFloat(Keys.AMOUNT, 0f).toString().toFloat(),
            Keys.AMOUNT_MIN to intent.extras?.getFloat(Keys.AMOUNT_MIN, 0f).toString().toFloat(),
            Keys.FLETE to intent.extras?.getString(Keys.FLETE, Keys.ERROR).toString()
        )

        //Value of format and more string with format
        val format = Keys.FORMAT_PRICE
        val valueBase = data[Keys.VALUE_BASE].toString().toFloat()
        var isRepeated: Boolean
        var valueRepeated = -100f

        //Variables of screen (textView) with default data
        val type :TextView = findViewById(R.id.type)
        val category :TextView = findViewById(R.id.categoryProduct)
        val valueAmountView :TextInputEditText = findViewById(R.id.amount)
        val valueUnitView :TextInputEditText = findViewById(R.id.valueUnit)

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
                val ch = "/"
                var cleanString: String? = s.toString().replace("[$.,/und\\s]".toRegex(), "")

                if(cleanString.isNullOrEmpty()) cleanString = "0"

                if (ch in s.toString() && s.toString().length == 3){
                    amount = toDecimal(cleanString)
                    val textFormat = formatTextAmountFractions(s.toString())
                    valueAmountView.setText(textFormat)
                    valueAmountView.setSelection(textFormat.length - 4)
                }else{
                    amount = cleanString.toFloat()
                    val textFormat = formatTextAmount(cleanString)
                    valueAmountView.setText(textFormat)
                    valueAmountView.setSelection(textFormat.length - 4)
                }
                valueAmountView.addTextChangedListener(this)
            }

        })


        //assign default values (variables of the screen)
        type.text = data[Keys.NAME].toString()
        category.text = data[Keys.CATEGORY].toString()

        //disable textView
        type.isEnabled = false
        category.isEnabled = false


        //Change value of name Profile (data in memory cache)
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString(Keys.PROFILE, null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables buttons
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)
        findViewById<ImageButton?>(R.id.btnBack).setOnClickListener {
            finish()
        }


        //assign button values
        btnSeeUser = findViewById(R.id.btnNameUserAdd)
        btnSeeUser.setOnClickListener {
            val intent = Intent(this, ListUsers::class.java).apply {
                putExtra(Keys.CREDIT_SALE, true)
            }
            responseLauncher.launch(intent)
            btnSeeUser.error = null
        }


        //Click Buttons
        btnContinue.setOnClickListener {
            //Variables de los TextInputLayout
            val inputValueUnit: TextInputLayout = findViewById(R.id.inputValueUnit)
            val inputValueAmount: TextInputLayout = findViewById(R.id.inputAmount)

            //Variables de los text del TextInputLayout
            val valueAmount: String = amount.toString()
            val valueUnit: String = valueUnitView.text.toString().replace("[$.,COP\\s]".toRegex(), "")

            //Arrays (2) With data of the fields
            val texts: Vector<String> = Vector(
                listOf(
                    valueUnit,
                    valueAmount
                )
            )
            val inputsLayouts: Vector<TextInputLayout> = Vector(
                listOf(
                    inputValueUnit,
                    inputValueAmount
                )
            )

            //Validate selection Client
            if (btnSeeUser.text.toString() == R.string.txtNameClient.toString() || btnSeeUser.text == null || btnSeeUser.text == ""){
                btnSeeUser.error = Keys.ERROR_WITHOUT_CLIENT
                return@setOnClickListener
            }else{
                btnSeeUser.error = null
            }

            isRepeated = valueRepeated != valueUnit.toFloatOrNull()

            //Validate data of the screen with the Inventory
            if (valueAmount.isNotEmpty() && data[Keys.AMOUNT].toString().toFloat() < valueAmount.toFloat()) {
                inputValueAmount.isErrorEnabled = true
                inputValueAmount.error = Keys.ERROR_WITHOUT_INVENTORY
                return@setOnClickListener
            }else{
                inputValueAmount.isErrorEnabled = false
            }

            var continueScreen = true

            if(valueUnit.isNotEmpty() && valueBase > valueUnit.toFloat() && isRepeated) {
                continueScreen = false
                Alerts().showAlertSelection(
                    layoutInflater,
                    context,
                    String.format(Keys.ALERT_MSM_VALUE_MIN, format.format(valueBase)),
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

            //validate empty fields
            if (!ValidateEmpty().validate(texts, inputsLayouts)) {
                return@setOnClickListener
            }

            val amountInventory = data[Keys.AMOUNT].toString().toFloat() - valueAmount.toFloat()

            if(continueScreen){
                val intent = Intent(context, Receipt::class.java).apply {
                    putExtra(Keys.NAME_CLIENT, btnSeeUser.text)
                    putExtra(Keys.NUMBER_CLIENT, numberText)
                    putExtra(Keys.CATEGORY, data[Keys.CATEGORY].toString())
                    putExtra(Keys.VALUE_UNIT, valueUnit.toFloat())
                    putExtra(Keys.AMOUNT, valueAmount.toFloat())
                    putExtra(Keys.TYPE, data[Keys.NAME].toString())
                    putExtra(Keys.TITLE, Keys.CREDIT_SALE)
                    putExtra(Keys.KEY_INVENTORY, data[Keys.KEY].toString())
                    putExtra(Keys.PROVIDER, data[Keys.PROVIDER].toString())
                    putExtra(Keys.FLETE, data[Keys.FLETE].toString())
                    putExtra(Keys.AMOUNT_INVENTORY, amountInventory)
                }
                startActivity(intent)
                finish()
            }
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

    private fun formatTextAmountFractions(text: String): String {
        return Keys.FORMAT_AMOUNT_FRACTION.format(text)
    }

    private fun toDecimal(fraction: String): Float{
        val values: CharArray = fraction.toCharArray()
        return values[0].toString().toFloat()/ values[1].toString().toFloat()
    }

}
