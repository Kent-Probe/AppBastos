package com.kent.appbastos.model.firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

class DataBaseShareData () {

    val database = Firebase.database.reference

/*
    fun writeNewUser(name: String, number: String) {
        val user = User(name, number)
        database.child("users").push().setValue(user)
    }

    fun writeDebts(debts: Debts, nameClient: String){
        database.child("debts").child(nameClient).push().setValue(debts)
    }
*/
    fun writeNewCreditSale(amount: Float, valueUnit: Float, nameClient: String) {
        val debut = amount * valueUnit
        val creditSale = CreditSale(amount, valueUnit, debut)
        database.child("creditSale").child(nameClient).setValue(creditSale)
    }

    fun writeNewCashSale(){
        val cashSale = CashSale(0f, 0f)
        database.child("cashSale").setValue(cashSale)
    }
}

data class Debts( val debts: Float, val dateTime: LocalDateTime, val amount: Int, val valueUnit: Float, val valueTotal: Float)

data class CashSale(val amount: Float, val valueUnit: Float)

data class CreditSale(val amount: Float, val valueUnit: Float, val debut: Float)

data class User(val username: String? = null, val number: String? = null, val numberDebts: Float? = 0f, val debts: Float? = 0f) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
