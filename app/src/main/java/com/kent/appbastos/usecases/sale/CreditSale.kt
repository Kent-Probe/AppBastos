package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
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

        //assign button values
        btnSeeUser = findViewById(R.id.btnNameUserAdd)
        btnSeeUser.setOnClickListener {
            val intent = Intent(this, ListUsers::class.java).apply {
                putExtra("creditSale", true)
            }
            responseLauncher.launch(intent)
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

            val intent = Intent(this, AddRemarks::class.java).apply {
                putExtra("nameClient", btnSeeUser.text)
                putExtra("numberClient", numberText)
                putExtra("nameProduct", nameProduct)
                putExtra("valueUnit", valueUnit.toFloat())
                putExtra("valueAmount", valueAmount.toInt())
                putExtra("type", type.selectedItem.toString())
                putExtra("title", "creditSale")
            }
            startActivity(intent)
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

}