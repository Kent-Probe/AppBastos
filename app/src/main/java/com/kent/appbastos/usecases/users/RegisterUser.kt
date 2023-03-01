package com.kent.appbastos.usecases.users

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.kent.appbastos.R
import com.kent.appbastos.model.BasicEventCallback
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.validate.ValidateEmpty
import java.util.*

class RegisterUser : AppCompatActivity() {

    //DataBase
    var database: DataBaseShareData = DataBaseShareData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)


        //Buttons of the layer
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        //Fields of the layer
        var nameUser: TextInputEditText
        var number: TextInputEditText

        //Fields text input layout
        val inputNameUser: TextInputLayout = findViewById(R.id.inputNameUser)
        val inputNumber: TextInputLayout = findViewById(R.id.inputNumberUser)

        //Arrays of the values
        val inputLayout: Vector<TextInputLayout> = Vector()
        val texts: Vector<String> = Vector()


        btnCancel.setOnClickListener {
            finish()
        }

        btnContinue.setOnClickListener {
            //assign values at fields
            nameUser = findViewById(R.id.nameUser)
            number = findViewById(R.id.numberUser)

            val nameUserText = nameUser.text.toString()
            val numberText = number.text.toString()
            //assign values at arrays
            inputLayout.addAll(
                listOf(
                    inputNumber,
                    inputNameUser
                )
            )
            texts.addAll(
                listOf(
                    numberText,
                    nameUserText
                )
            )
            val toastSuccess: Toast = Toast.makeText(this, "Se grabo correctamente", Toast.LENGTH_LONG)
            val toastCancel: Toast = Toast.makeText(this, "Se encontro una instancia", Toast.LENGTH_LONG)
            val toastFailure: Toast = Toast.makeText(this, "Error en base de datos", Toast.LENGTH_LONG)
            if(ValidateEmpty().validate(texts, inputLayout)){
                DataBaseShareData().checkClientExist(nameUser.text.toString(), object : BasicEventCallback{
                    override fun onSuccess(dataSnapshot: DataSnapshot) {
                        if(!dataSnapshot.exists()){
                            database.writeNewUser(name = nameUserText, number = numberText)
                            toastSuccess.show()
                            finish()
                        }
                    }

                    override fun databaseFailure() {
                        toastFailure.show()
                    }

                    override fun onCancel() {
                        toastCancel.show()
                    }
                })
            }
        }
        setUpDate()
    }

    //SetUp Date of te fields
    private fun setUpDate(){
        //assign values at buttons

    }
}