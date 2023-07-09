package com.kent.appbastos.usecases.mainPrincipal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kent.appbastos.R
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.usecases.dashboard.DashBoard
import com.kent.appbastos.usecases.debts.Payment
import com.kent.appbastos.usecases.sale.MenuSale
import com.kent.appbastos.usecases.sale.OperationalExpenses

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        //Variables of share data
        val bundle = intent.extras
        val email = bundle?.getString(Keys.EMAIL)
        val provider = bundle?.getString(Keys.PROVIDER)
        val profile = bundle?.getString(Keys.PROFILE)
        val rol = bundle?.getString(Keys.ROL)

        //Guardar Datos
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        pref.putString(Keys.EMAIL, email)
        pref.putString(Keys.PROVIDER, provider)
        pref.putString(Keys.PROFILE, profile)
        pref.putString(Keys.ROL, rol)
        pref.apply()

        //Variables of buttons
        val btnRegisterSale: Button = findViewById(R.id.btnRegisterSale)
        val btnAddPayment: Button = findViewById(R.id.btnAddPayment)
        val btnOperationalExpenses: Button = findViewById(R.id.btnOperationalExpenses)
        val btnLogOut: Button = findViewById(R.id.btnLogOut)
        val btnDashBoard: Button = findViewById(R.id.btnDashBoard)

        //Variables of text view
        val textUserView: TextView = findViewById(R.id.txtUserName)

        val nameProfile = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).getString(Keys.PROFILE, null).toString()
        textUserView.text = nameProfile
        btnLogOut.setOnClickListener {
            //Deleted Dates
            val preferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            preferences.remove(Keys.EMAIL)
            preferences.remove(Keys.PROVIDER)
            preferences.remove(Keys.PROFILE)
            preferences.remove(Keys.ROL)
            preferences.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        btnRegisterSale.setOnClickListener {
            val intent = Intent(this, MenuSale::class.java)
            startActivity(intent)
        }

        btnOperationalExpenses.setOnClickListener {
            val intent = Intent(this, OperationalExpenses::class.java)
            startActivity(intent)
        }

        btnDashBoard.setOnClickListener {
            val intent = Intent(this, DashBoard::class.java)
            startActivity(intent)
        }

        btnAddPayment.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            startActivity(intent)
        }


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if (keyCode == event.keyCode) run {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("¿Desea salir?")
                    .setPositiveButton("si") { dialog, which ->
                        finishAffinity()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }
                alertDialog.show()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}