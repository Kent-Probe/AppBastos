package com.kent.appbastos.usecases.sale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kent.appbastos.R
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.validate.ValidateEmpty
import com.kent.appbastos.usecases.remarks.AddRemarks
import java.util.*


class CreditSale : AppCompatActivity() {

    private val database = Firebase.database.reference

    private lateinit var nameClientView: Button
    private lateinit var numberClientView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_sale)

        readUser(this)

        nameClientView = findViewById(R.id.nameClient)
        numberClientView = findViewById(R.id.numberClient)

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

        //Click Buttons
        btnContinue.setOnClickListener {

            //Variables de los TextInputLayout
            val inputNumberClient: TextInputLayout = findViewById(R.id.inputNumberClient)
            val inputNameProduct: TextInputLayout = findViewById(R.id.inputNameProduct)
            val inputValueUnit: TextInputLayout = findViewById(R.id.inputValueUnit)
            val inputValueAmount: TextInputLayout = findViewById(R.id.inputAmount)

            inputNumberClient.visibility = View.VISIBLE

            //Variables del  editText
            val nameProductView: TextView = findViewById(R.id.nameProduct)
            val valueUnitView: TextView = findViewById(R.id.valueUnit)
            val valueAmountView: TextView = findViewById(R.id.amount)

            //Variables de los text del TextInputLayout

            val nameProduct: String = nameProductView.text.toString()
            val valueUnit: String = valueUnitView.text.toString()
            val valueAmount: String = valueAmountView.text.toString()

            //Arrays
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

            if (ValidateEmpty().validate(texts, inputsLayouts)) {
                val view: View = Alerts().showAlertPersonalize(
                    layoutInflater,
                    this,
                    R.string.txtAlertDialog.toString(),
                    "Abrir una nueva deuda",
                    "Agregar pago"
                )

                val btnUp: Button = view.findViewById(R.id.btnUp)
                val btnDown: Button = view.findViewById(R.id.btnDown)

                btnUp.setOnClickListener {
                    val intent = Intent(this, AddRemarks::class.java).apply {
                        putExtra("nameClient", "nameClient")
                        putExtra("numberClient", "numberClient")
                        putExtra("nameProduct", nameProduct)
                        putExtra("valueUnit", valueUnit.toFloat())
                        putExtra("valueAmount", valueAmount.toInt())
                        putExtra("type", type.selectedItem.toString())
                        putExtra("title", "creditSale")
                    }
                    startActivity(intent)
                    finish()
                }
                btnDown.setOnClickListener {
                    val intent = Intent(this, Payment::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun readUser(context: Context){
        database.child("users").child("clients").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items: ArrayList<String> = ArrayList()
                snapshot.children.forEach { child ->
                    val user = child.child("username").value.toString()
                    items.add(user)
                }
                val users:Array<String> = Array(snapshot.childrenCount.toInt()){
                    items[it]
                }

                nameClientView.setOnClickListener {
                    Alerts().showAlertSelection(
                        layoutInflater,
                        context,
                        "Clientes registrados",
                        "Registrar un nuevo cliente",
                        "Calcelar",
                        users,
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //listUser.clear()
            }
        })
    }
}