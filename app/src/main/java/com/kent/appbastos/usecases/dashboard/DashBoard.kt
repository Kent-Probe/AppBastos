package com.kent.appbastos.usecases.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kent.appbastos.R
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.usecases.inventory.listInventory.ListInventory
import com.kent.appbastos.usecases.users.clients.ListUsers
import com.kent.appbastos.usecases.users.usersApp.ListUserApp

class DashBoard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        //Change at user name
        //Variables of text view
        val textUserView: TextView = findViewById(R.id.txtUserName)

        //Variables of the button
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnRegisterUser: Button = findViewById(R.id.btnRegisterUser)
        val btnListInventory: Button = findViewById(R.id.btnListInventory)
        val btnAdminUserApp: Button = findViewById(R.id.btnAdminUserApp)

        //Hide Button only admin
        btnAdminUserApp.visibility = Button.GONE

        val nameProfile = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).getString( Keys.PROFILE, null).toString()
        val rol = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).getString(Keys.ROL, null).toString()
        textUserView.text = nameProfile

        //Show button only admin
        if(rol == Keys.ROL_ADMIN){
            btnAdminUserApp.visibility = Button.VISIBLE
        }

        btnRegisterUser.setOnClickListener {
            val intent = Intent(this, ListUsers::class.java)
            startActivity(intent)
        }

        btnListInventory.setOnClickListener {
            val intent= Intent(this, ListInventory::class.java)
            startActivity(intent)
        }

        btnAdminUserApp.setOnClickListener{
            val intent = Intent(this, ListUserApp::class.java)
            startActivity(intent)
        }



        btnBack.setOnClickListener {
            finish()
        }
    }
}