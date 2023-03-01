package com.kent.appbastos.usecases.remarks

import android.content.Context
import android.content.Intent
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
import com.kent.appbastos.model.firebase.Debts
import com.kent.appbastos.model.values.CashSaleClass
import com.kent.appbastos.model.values.CreditSaleClass
import com.kent.appbastos.usecases.share.Share
import java.time.LocalDateTime


class AddRemarks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remarks)

        //Variables Temp
        val txtTempTEXT: String
        val title = intent.extras?.getString("title")

        //Texts of field
        val txtRemarks: TextView = findViewById(R.id.txtRemarks)

        //Variable sof class
        var dateCashSaleClass = CashSaleClass()
        var dateCreditSaleClass = CreditSaleClass()

        if(title.toString() == "CashSale"){
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
        val profile = pref.getString("profile", null).toString()
        val txtUserName:TextView = findViewById(R.id.txtUserName)

        txtUserName.text = profile

        //Buttons
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnContinue.setOnClickListener {
            if(title.toString() == "CashSale"){
                if(dateCashSaleClass.saveArchive(this)){
                    Toast.makeText(this, "Se grabo correctamente", Toast.LENGTH_LONG).show()
                }
                else Toast.makeText(this, "No se guardo", Toast.LENGTH_LONG).show()
            }else{
                if(dateCreditSaleClass.saveArchive(this)){
                    //Save data in the database
                    val debts = Debts(
                        dateCreditSaleClass.valueTotal,
                        dateCreditSaleClass.dateTime,
                        dateCreditSaleClass.valueAmount.toInt(),
                        dateCreditSaleClass.valueUnit,
                        dateCreditSaleClass.valueTotal)
                    DataBaseShareData().writeDebts(debts, dateCreditSaleClass.nameClient)

                    Toast.makeText(this, "Se grabo correctamente", Toast.LENGTH_LONG).show()
                }
                else Toast.makeText(this, "No se guardo", Toast.LENGTH_LONG).show()
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
        //Text of topics
        val nameClient = this.intent.extras?.getString("nameClient").toString()
        val nameProduct = this.intent.extras?.getString("nameProduct").toString()
        val valueUnit = this.intent.extras?.getFloat("valueUnit").toString()
        val valueAmount = this.intent.extras?.getInt("valueAmount").toString()
        val type = this.intent.extras?.getString("type").toString()

        DataBaseShareData().writeNewCashSale()

        //Date for the class
        return CashSaleClass(
            nameClient,
            nameProduct,
            "MARCA",
            valueAmount.toFloat(),
            valueUnit.toFloat(),
            valueUnit.toFloat() * valueAmount.toInt(),
            LocalDateTime.now(),
            "#Consecutivo",
            type
        )
    }
    private fun fillClassCredit(): CreditSaleClass{
        //Text of topics
        val nameClient = this.intent.extras?.getString("nameClient").toString()
        val numberClient = this.intent.extras?.getString("numberClient").toString()
        val nameProduct = this.intent.extras?.getString("nameProduct").toString()
        val valueUnit = this.intent.extras?.getFloat("valueUnit").toString()
        val valueAmount = this.intent.extras?.getInt("valueAmount").toString()
        val type = this.intent.extras?.getString("type").toString()
        val total = valueUnit.toFloat() * valueAmount.toInt()
        val localDataTIme: LocalDateTime = LocalDateTime.now()

        return CreditSaleClass(
            "MARCA",
            nameClient,
            numberClient,
            nameProduct,
            valueUnit.toFloat(),
            valueAmount.toFloat(),
            total,
            localDataTIme,
            "#Consecutivo",
            type
        )
    }

    private fun readData(txtView: TextView){
        var string = ""
        val firebase = Firebase.database.reference
        val eventListener: ValueEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("creditSale").children.forEach{child ->
                    string += "\n---------------------"
                    string += "\n\t" + child.key
                    string += "\n\t" + child.child("amount").value.toString()
                }
                txtView.text = string
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Error")
                string = "Error"
            }
        }
        firebase.addValueEventListener(eventListener)
    }

}
