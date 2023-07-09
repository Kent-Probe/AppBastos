package com.kent.appbastos.usecases.remarks

import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.DateTime
import com.kent.appbastos.model.firebase.Debts
import com.kent.appbastos.model.firebase.InventoryOfDebts
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.values.CashSaleClass
import com.kent.appbastos.model.values.CreditSaleClass
import com.kent.appbastos.usecases.share.Share


class AddRemarks : AppCompatActivity() {

    private lateinit var keyInventory: String
    private lateinit var newAmount: String

    override fun onStart() {
        super.onStart()
        //Key Inventory and new amount
        keyInventory = intent.extras?.getString(Keys.KEY_INVENTORY).toString()
        newAmount = intent.extras?.getFloat(Keys.AMOUNT_INVENTORY).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remarks)

        //Key Inventory and new amount and more inventory
        keyInventory = intent.extras?.getString(Keys.KEY_INVENTORY).toString()
        newAmount = intent.extras?.getString(Keys.AMOUNT_INVENTORY).toString()
        val category = intent.extras?.getString(Keys.CATEGORY, "")
        val provider = intent.extras?.getString(Keys.PROVIDER, "")
        val name = intent.extras?.getString(Keys.TYPE, "")
        val flete = intent.extras?.getString(Keys.FLETE, "")

        //Variables Temp
        val txtTempTEXT: String
        val title = intent.extras?.getString(Keys.TITLE)

        //Texts of field
        val txtRemarks: TextView = findViewById(R.id.txtRemarks)

        //Variable sof class
        var dateCashSaleClass = CashSaleClass()
        var dateCreditSaleClass = CreditSaleClass()

        if(title.toString() == Keys.CASH_SALE){
            //Date for the class
            dateCashSaleClass = fillClassCash()
            txtTempTEXT = dateCashSaleClass.dateOfClass()

        }else{
            dateCreditSaleClass = fillClassCredit()
            txtTempTEXT = dateCreditSaleClass.dateOfClass()

        }
        readData(txtRemarks)
        txtRemarks.text = txtTempTEXT

        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString(Keys.PROFILE, null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)

        txtUserName.text = profile

        //Buttons
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)


        //Button continue
        btnContinue.setOnClickListener {
            if(title.toString() == Keys.CASH_SALE){
                if(dateCashSaleClass.saveArchive(this)){
                    Toast.makeText(this, Keys.TOAST_ADD_SUCCESSFULLY, Toast.LENGTH_LONG).show()
                }
                else Toast.makeText(this, Keys.TOAST_NOT_ADD, Toast.LENGTH_LONG).show()
            }else{
                if(dateCreditSaleClass.saveArchive(this)){

                    //Save data in the database
                    val debts = Debts(
                        debts = dateCreditSaleClass.valueTotal,
                        dateTime = dateCreditSaleClass.dateTime,
                        amount = dateCreditSaleClass.valueAmount.toInt(),
                        valueUnit = dateCreditSaleClass.valueUnit,
                        valueTotal = dateCreditSaleClass.valueTotal,
                        inventoryOfDebts = InventoryOfDebts(
                            category = category.toString(),
                            name = name.toString(),
                            provider = provider.toString(),
                            flete = flete.toString(),
                            key = keyInventory
                        )
                    )
                    DataBaseShareData().writeDebts(this, debts, dateCreditSaleClass.nameClient, keyInventory, newAmount)

                    Toast.makeText(this, Keys.TOAST_ADD_SUCCESSFULLY, Toast.LENGTH_LONG).show()
                }
                else Toast.makeText(this, Keys.TOAST_NOT_ADD, Toast.LENGTH_LONG).show()
            }

            val intent = Intent(this, Share::class.java).apply {
                putExtra("text", txtTempTEXT)
            }
            startActivity(intent)
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun fillClassCash(): CashSaleClass {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //Text of topics
        val nameClient = this.intent.extras?.getString(Keys.NAME_CLIENT).toString()
        val category = this.intent.extras?.getString(Keys.CATEGORY).toString()
        val valueUnit = this.intent.extras?.getFloat(Keys.VALUE_UNIT).toString()
        val valueAmount = this.intent.extras?.getInt(Keys.VALUE_AMOUNT).toString()
        val type = this.intent.extras?.getString(Keys.TYPE).toString()

        DataBaseShareData().writeNewCashSale()

        //Date for the class
        return CashSaleClass(
            nameClient,
            category,
            "MARCA",
            valueAmount.toFloat(),
            valueUnit.toFloat(),
            valueUnit.toFloat() * valueAmount.toInt(),
            DateTime(day, month, year),
            "#Consecutivo",
            type
        )
    }
    private fun fillClassCredit(): CreditSaleClass{
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //Text of topics
        val nameClient = this.intent.extras?.getString(Keys.NAME_CLIENT).toString()
        val numberClient = this.intent.extras?.getString(Keys.NUMBER_CLIENT).toString()
        val category = this.intent.extras?.getString(Keys.CATEGORY).toString()
        val valueUnit = this.intent.extras?.getFloat(Keys.VALUE_UNIT).toString()
        val valueAmount = this.intent.extras?.getInt(Keys.VALUE_AMOUNT).toString()
        val type = this.intent.extras?.getString(Keys.TYPE).toString()
        val total = valueUnit.toFloat() * valueAmount.toInt()
        val dateTime = DateTime(day, month, year)

        return CreditSaleClass(
            "MARCA",
            nameClient,
            numberClient,
            category,
            valueUnit.toFloat(),
            valueAmount.toFloat(),
            total,
            dateTime,
            "#Consecutivo",
            type
        )
    }

    private fun readData(txtView: TextView){
        var string = ""
        val firebase = Firebase.database.reference
        val eventListener: ValueEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child(Keys.CREDIT_SALE).children.forEach{child ->
                    string += "\n---------------------"
                    string += "\n\t" + child.key
                    string += "\n\t" + child.child(Keys.AMOUNT).value.toString()
                }
                txtView.text = string
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Error en add remarks")
                string = "Error"
            }
        }
        firebase.addValueEventListener(eventListener)
    }

}
