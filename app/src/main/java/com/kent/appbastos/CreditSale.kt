package com.kent.appbastos
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class CreditSale : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_sale)

        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnContinue.setOnClickListener {
            val dialogAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            val layoutInflater: LayoutInflater = layoutInflater
            val view: View = layoutInflater.inflate(R.layout.dialog_custom, null)

            dialogAlertDialog.setView(view)
            val alertDialog: AlertDialog = dialogAlertDialog.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btnOpenAddDebt: Button = view.findViewById(R.id.btnOpenNewDebt)
            val btnAddPayment: Button = view.findViewById(R.id.btnAddPayment)

            btnOpenAddDebt.setOnClickListener {
                val intent: Intent = Intent(this, AddRemarks::class.java)
                startActivity(intent)
            }
            btnAddPayment.setOnClickListener {
                val intent: Intent = Intent(this, Payment::class.java)
                startActivity(intent)
            }

            alertDialog.show()
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}