package com.kent.appbastos.usecases.mainPrincipal

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.kent.appbastos.R
import com.kent.appbastos.usecases.sale.OperationalExpenses
import com.kent.appbastos.usecases.sale.MenuSale

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        //Variables of share data
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        val profile = bundle?.getString("profile")


        //Guardar Datos
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        pref.putString("email", email)
        pref.putString("provider", provider)
        pref.putString("profile", profile)
        pref.apply()

        //Variables of buttons
        val btnRegisterSale: Button = findViewById(R.id.btnRegisterSale)
        val btnOperationalExpenses: Button = findViewById(R.id.btnOperationalExpenses)
        val btnLogOut: Button = findViewById(R.id.btnLogOut)

        //Variables of text view
        val textUserView: TextView = findViewById(R.id.txtUserName)

        val nameProfile = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).getString("profile", null).toString()
        textUserView.text = nameProfile
        btnLogOut.setOnClickListener {
            //Deleted Dates
            val preferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            preferences.remove("email")
            preferences.remove("provider")
            preferences.remove("profile")
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

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if (keyCode == event.keyCode) run {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("¿Desea salir?")
                    .setPositiveButton("si", DialogInterface.OnClickListener { dialog, which ->
                        finishAffinity()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                alertDialog.show()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}