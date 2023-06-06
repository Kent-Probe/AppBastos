package com.kent.appbastos.usecases.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.usecases.inventory.addInventory.AddInventory
import com.kent.appbastos.usecases.users.ListUsers

class DashBoard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        //Change at user name
        //Variables of text view
        val textUserView: TextView = findViewById(R.id.txtUserName)

        //Variables of the button
        val btnRegisterUser: Button = findViewById(R.id.btnRegisterUser)
        val btnAddInventory: Button = findViewById(R.id.btnAddInventory)
        val btnBack: ImageButton = findViewById(R.id.btnBack)

        //HIDE IMPORTANT
        val btnDel: Button = findViewById(R.id.btnDelBasedatos)
        btnDel.visibility = Button.GONE

        btnDel.setOnClickListener {
            DataBaseShareData().delDatabase(this)

        }

        val nameProfile = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).getString("profile", null).toString()
        textUserView.text = nameProfile

        btnRegisterUser.setOnClickListener {
            val intent = Intent(this, ListUsers::class.java)
            startActivity(intent)
        }

        btnAddInventory.setOnClickListener {
            val intent = Intent(this, AddInventory::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}