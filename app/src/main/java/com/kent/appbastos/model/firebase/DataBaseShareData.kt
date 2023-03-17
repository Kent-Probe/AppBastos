package com.kent.appbastos.model.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kent.appbastos.model.BasicEventCallback
import java.time.LocalDateTime

class DataBaseShareData () {

    val database = Firebase.database.reference

    companion object{
        //title of DATABASE
        const val DEBTS = "debts"
        const val USERS = "users"
        const val CLIENTS = "clients"

        //title of DATABASE date
        const val USERNAME = "username"
        const val DEBTS_TOTAL = "debts"
        const val NUMBER = "number"
        const val NUMBER_DEBTS = "numberDebts"
        const val VALUE_TOTAL = "valueTotal"

        //key value
        const val KEY = "key"
    }
    private fun increment(valueS: String, increment: Float): Float{
        return valueS.toFloat() + increment
    }

    //Write payment
    fun addPayment(nameClient: String, key: String, valueTotal: Float){
        database.child(DEBTS).child(nameClient).child(key).child(VALUE_TOTAL).setValue(valueTotal)
    }

    //Write new debts
    fun writeDebts(debts: Debts, nameClient: String){
        database.child(DEBTS).child(nameClient).push().setValue(debts)
        checkClientExist(nameClient, object : BasicEventCallback{
            override fun onSuccess(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { child ->
                    val updates: MutableMap<String, Any> = hashMapOf(
                        DEBTS_TOTAL to increment(child.child(DEBTS_TOTAL).value.toString(), debts.valueTotal),
                        NUMBER to child.child(NUMBER).value.toString(),
                        NUMBER_DEBTS to increment(child.child(NUMBER_DEBTS).value.toString(), 1f),
                        USERNAME to child.child(USERNAME).value.toString()
                    )
                    database.child(USERS).child(CLIENTS).child(child.key.toString()).setValue(updates)
                }
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun databaseFailure() {
                TODO("Not yet implemented")
            }

        })
    }

    //Write new user
    fun writeNewUser(name: String, number: String) {
        val user = User(name, number)
        database.child(USERS).child(CLIENTS).push().setValue(user)
    }

    //Write new cash sale
    fun writeNewCashSale(){
        val cashSale = CashSale(0f, 0f)
        database.child("cashSale").setValue(cashSale)
    }

    //Function for check exit user
    fun checkClientExist(nameClient: String, callBack: BasicEventCallback){
        val databaseUser = database.child(USERS).child(CLIENTS)

        val userByNameClient: Query = databaseUser.orderByChild(USERNAME).equalTo(nameClient).limitToFirst(1)

        userByNameClient.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                callBack.onSuccess(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                callBack.databaseFailure()
            }
        })
    }


}

data class Debts( val debts: Float, val dateTime: LocalDateTime, val amount: Int, val valueUnit: Float, val valueTotal: Float, val key: String? = null)

data class CashSale(val amount: Float, val valueUnit: Float)

data class CreditSale(val amount: Float, val valueUnit: Float, val debut: Float)

data class User(val username: String? = null, val number: String? = null, val numberDebts: Float? = 0f, val debts: Float? = 0f, val key:String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
