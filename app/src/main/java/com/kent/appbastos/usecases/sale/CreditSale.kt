package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.util.EventButtonsCallBack
import com.kent.appbastos.model.validate.ValidateEmpty
import com.kent.appbastos.usecases.remarks.AddRemarks
import com.kent.appbastos.usecases.users.ListUsers
import com.kent.appbastos.usecases.users.ListUsers.Companion.PHONE
import com.kent.appbastos.usecases.users.ListUsers.Companion.USERNAME
import java.text.DecimalFormat
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
            inputNumberClient.visibility = View.VISIBLE
            numberClientView.text = it.data?.getStringExtra(PHONE)
            btnSeeUser.text = it.data?.getStringExtra(USERNAME)
            numberText = it.data?.getStringExtra(PHONE).toString()
        }
    }

    companion object{
        const val KEY = "key"
        const val PROVIDER = "provider"
        const val NAME = "name"
        const val CATEGORY = "category"
        const val VALUE_BASE = "valueBase"
        const val AMOUNT = "amount"
        const val AMOUNT_MIN = "amountMin"

        const val ERROR = "error"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_sale)

        //variable util
        val context: Context = this
        val layoutInflater = layoutInflater

        //variable input text
        numberClientView = findViewById(R.id.numberClient)
        inputNumberClient = findViewById(R.id.inputNumberClient)

        //variable intent, data of the listInventory (Screen)
        val data :MutableMap<String, Any> = hashMapOf(
            KEY to intent.extras?.getString(KEY, ERROR).toString(),
            NAME to intent.extras?.getString(NAME, ERROR).toString(),
            PROVIDER to intent.extras?.getString(PROVIDER, ERROR).toString(),
            CATEGORY to intent.extras?.getString(CATEGORY, ERROR).toString(),
            VALUE_BASE to intent.extras?.getFloat(VALUE_BASE, 0f).toString().toFloat(),
            AMOUNT to intent.extras?.getFloat(AMOUNT, 0f).toString().toFloat(),
            AMOUNT_MIN to intent.extras?.getFloat(AMOUNT_MIN, 0f).toString().toFloat()
        )

        //Value of format and more string with format
        val format = DecimalFormat("$#,### COP")
        val valueBase = data[VALUE_BASE].toString().toFloat()
        var isRepeated: Boolean
        var valueRepeated = -100f

        //Variables of screen (textView) with default data
        val type:TextView = findViewById(R.id.type)
        val category:TextView = findViewById(R.id.nameProduct)

        //assign default values (variables of the screen)
        type.text = data[NAME].toString()
        category.text = data[CATEGORY].toString()

        //disable textView
        type.isEnabled = false
        category.isEnabled = false


        //Change value of name Profile (data in memory cache)
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString("profile", null).toString()
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
                putExtra("creditSale", true)
            }
            responseLauncher.launch(intent)
            btnSeeUser.error = null
        }


        //Click Buttons
        btnContinue.setOnClickListener {
            //Variables de los TextInputLayout
            val inputValueUnit: TextInputLayout = findViewById(R.id.inputValueUnit)
            val inputValueAmount: TextInputLayout = findViewById(R.id.inputAmount)

            //Variables del  editText
            val valueUnitView: TextView = findViewById(R.id.valueUnit)
            val valueAmountView: TextView = findViewById(R.id.amount)

            //Variables de los text del TextInputLayout
            val valueUnit: String = valueUnitView.text.toString()
            val valueAmount: String = valueAmountView.text.toString()

            //Arrays -2
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

            if (btnSeeUser.text.toString() == R.string.txtNameClient.toString() || btnSeeUser.text == null || btnSeeUser.text == ""){
                btnSeeUser.error = "Debe seleccionar un cliente"
                return@setOnClickListener
            }else{
                btnSeeUser.error = null
            }

            isRepeated = valueRepeated != valueUnit.toFloatOrNull()

            //Validate data of the screen with the Inventory
            if (valueAmount.isNotEmpty() && data[AMOUNT].toString().toFloat() < valueAmount.toFloat()) {
                inputValueAmount.isErrorEnabled = true
                inputValueAmount.error = "No hay suficiente inventario"
                return@setOnClickListener
            }else{
                inputValueAmount.isErrorEnabled = false
            }

            if(valueUnit.isNotEmpty() && valueBase > valueUnit.toFloat() && isRepeated) {
                Alerts().showAlertSelection(
                    layoutInflater,
                    context,
                    "Alerta el valor minimo es de ${format.format(valueBase)}, desea continuar",
                    "Continuar",
                    "Cambiar",
                    object : EventButtonsCallBack {
                        override fun buttonUp(alertDialog: AlertDialog) {
                            valueRepeated = valueUnit.toFloat()
                            isRepeated = false
                            alertDialog.hide()
                        }

                        override fun buttonDown(alertDialog: AlertDialog) {
                            valueUnitView.text = ""
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

            val amountInventory = data[AMOUNT].toString().toFloat() - valueAmount.toFloat()

            val intent = Intent(context, AddRemarks::class.java).apply {
                putExtra("nameClient", btnSeeUser.text)
                putExtra("numberClient", numberText)
                putExtra("nameProduct", data[CATEGORY].toString())
                putExtra("valueUnit", valueUnit.toFloat())
                putExtra("valueAmount", valueAmount.toInt())
                putExtra("type", data[NAME].toString())
                putExtra("title", "creditSale")
                putExtra("keyInventory", data[KEY].toString())
                putExtra("amountInventory", amountInventory)

            }
            startActivity(intent)
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

}