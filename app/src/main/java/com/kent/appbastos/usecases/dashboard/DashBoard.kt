package com.kent.appbastos.usecases.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kent.appbastos.R
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
        val btnBack: ImageButton = findViewById(R.id.btnBack)

        val nameProfile = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).getString("profile", null).toString()
        textUserView.text = nameProfile

        btnRegisterUser.setOnClickListener {
            val intent = Intent(this, ListUsers::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}