package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.kent.appbastos.R
import com.kent.appbastos.model.BasicEventCallback
import com.kent.appbastos.model.EventButtonsCallBack
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.validate.ValidateEmpty
import com.kent.appbastos.usecases.remarks.AddRemarks
import com.kent.appbastos.usecases.users.ListUsers
import com.kent.appbastos.usecases.users.ListUsers.Companion.PHONE
import com.kent.appbastos.usecases.users.ListUsers.Companion.USERNAME
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_sale)

        //variable input text
        numberClientView = findViewById(R.id.numberClient)
        inputNumberClient = findViewById(R.id.inputNumberClient)

        //Spinner
        val type:Spinner = findViewById(R.id.type)

        //Options
        val options = arrayOf("v", "m")

        //Adapter
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options)

        //Adapter with the spinner
        type.adapter = adapter
        type.setSelection(0)


        //Change value of name Profile
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
            val inputNameProduct: TextInputLayout = findViewById(R.id.inputNameProduct)
            val inputValueUnit: TextInputLayout = findViewById(R.id.inputValueUnit)
            val inputValueAmount: TextInputLayout = findViewById(R.id.inputAmount)

            //Variables del  editText
            val nameProductView: TextView = findViewById(R.id.nameProduct)
            val valueUnitView: TextView = findViewById(R.id.valueUnit)
            val valueAmountView: TextView = findViewById(R.id.amount)

            //Variables de los text del TextInputLayout

            val nameProduct: String = nameProductView.text.toString()
            val valueUnit: String = valueUnitView.text.toString()
            val valueAmount: String = valueAmountView.text.toString()

            //Arrays -2
            val texts: Vector<String> = Vector(
                listOf(
                    nameProduct,
                    valueUnit,
                    valueAmount
                )
            )
            val inputsLayouts: Vector<TextInputLayout> = Vector(
                listOf(
                    inputNameProduct,
                    inputValueUnit,
                    inputValueAmount
                )
            )

            //validate empty fields
            if (!ValidateEmpty().validate(texts, inputsLayouts)) {
                return@setOnClickListener
            }

            if (btnSeeUser.text.toString() == R.string.txtNameClient.toString() || btnSeeUser.text == null || btnSeeUser.text == ""){
                btnSeeUser.error = "Debe seleccionar un cliente"
                return@setOnClickListener
            }
            btnSeeUser.error = null
            val itemType = type.selectedItem.toString()
            val context: Context = this
            val layoutInflater = layoutInflater

            DataBaseShareData().checkDebts(itemType, object : BasicEventCallback {
                override fun onSuccess(dataSnapshot: DataSnapshot) {
                    inputValueAmount.isErrorEnabled = false
                    inputValueUnit.isErrorEnabled = false

                    var amount: String? = ""
                    var valueBase: String? = ""

                    var key: String? = ""
                    dataSnapshot.children.forEach {
                        key = it.key.toString()
                        amount = it.child("amount").value.toString()
                        valueBase = it.child("valueBase").value.toString()

                    }

                    if(amount.isNullOrEmpty()){
                        Toast.makeText(context, "Error en la base de datos", Toast.LENGTH_LONG).show()
                        return
                    }

                    if(valueBase.isNullOrEmpty()){
                        Toast.makeText(context, "Error en la base de datos", Toast.LENGTH_LONG).show()
                        return
                    }

                    val amountDatabase = amount!!.toFloat()
                    val valueBaseDatabase = valueBase!!.toFloat()
                    val amountScreen = valueAmount.toFloat()
                    val valueUnitScreen = valueUnit.toFloat()

                    if(amountDatabase < amountScreen){
                        inputValueAmount.isErrorEnabled = true
                        inputValueAmount.error = "No hay suficiente inventario"
                        return
                    }

                    if(valueBaseDatabase > valueUnitScreen){
                        var validate = true
                        Alerts().showAlertSelection(
                            layoutInflater,
                            context,
                            "Alerta el valor minimo es de $valueBaseDatabase",
                            "Continuar",
                            "Cancelar",
                            object : EventButtonsCallBack {
                                override fun buttonUp(alertDialog: AlertDialog) {
                                    validate = false
                                    alertDialog.hide()
                                }

                                override fun buttonDown(alertDialog: AlertDialog) {
                                    valueUnitView.text = ""
                                    validate = true
                                    alertDialog.hide()
                                }

                            }
                        )
                        if(!validate) return
                    }

                    val amountInventory = amountDatabase - amountScreen

                    val intent = Intent(context, AddRemarks::class.java).apply {
                        putExtra("nameClient", btnSeeUser.text)
                        putExtra("numberClient", numberText)
                        putExtra("nameProduct", nameProduct)
                        putExtra("valueUnit", valueUnit.toFloat())
                        putExtra("valueAmount", valueAmount.toInt())
                        putExtra("type", itemType)
                        putExtra("title", "creditSale")
                        putExtra("keyInventory", key)
                        putExtra("amountInventory", amountInventory)
                    }
                    startActivity(intent)
                    finish()
                }

                override fun onCancel() {
                    Toast.makeText(context, "Error con el inventario", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun databaseFailure() {
                    Toast.makeText(context, "Datos agregados con exito", Toast.LENGTH_LONG).show()
                    finish()
                }

            })
            //finish()

        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

}