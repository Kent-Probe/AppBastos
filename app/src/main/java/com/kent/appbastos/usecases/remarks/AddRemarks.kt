package com.kent.appbastos.usecases.remarks

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.kent.appbastos.R
import com.kent.appbastos.model.values.CashSaleClass
import com.kent.appbastos.model.values.CreditSaleClass
import com.kent.appbastos.usecases.share.Share
import java.time.LocalDateTime


class AddRemarks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remarks)

        //Variables Temp
        var uriPath = ""
        var txtTempTEXT = ""
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
        val nameClient = this.intent.extras?.getString("nameClient")
        val nameProduct = this.intent.extras?.getString("nameProduct")
        val valueUnit = this.intent.extras?.getFloat("valueUnit")
        val valueAmount = this.intent.extras?.getInt("valueAmount")

        //Date for the class
        return CashSaleClass(
            nameClient.toString(),
            nameProduct.toString(),
            "MARCA",
            valueAmount.toString().toInt(),
            valueUnit.toString().toFloat(),
            valueUnit.toString().toFloat() * valueAmount.toString().toInt(),
            LocalDateTime.now(),
            "#Consecutivo"
        )
    }
    private fun fillClassCredit(): CreditSaleClass{
        //Text of topics
        val nameClient = this.intent.extras?.getString("nameClient")
        val numberClient = this.intent.extras?.getString("numberClient")
        val nameProduct = this.intent.extras?.getString("nameProduct")
        val valueUnit = this.intent.extras?.getFloat("valueUnit")
        val valueAmount = this.intent.extras?.getInt("valueAmount")

        return CreditSaleClass(
            "MARCA",
            nameClient.toString(),
            numberClient.toString(),
            nameProduct.toString(),
            valueUnit.toString().toFloat(),
            valueAmount.toString().toInt(),
            valueUnit.toString().toFloat() * valueAmount.toString().toInt(),
            LocalDateTime.now(),
            "#Consecutivo"
        )
    }
}
