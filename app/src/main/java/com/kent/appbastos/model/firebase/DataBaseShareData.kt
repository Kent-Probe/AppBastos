package com.kent.appbastos.model.firebase

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kent.appbastos.model.BasicEventCallback
import java.time.LocalDateTime
import java.util.*

class DataBaseShareData () {

    val database = Firebase.database.reference

    companion object{
        //title of DATABASE
        const val DEBTS = "debts"
        const val USERS = "users"
        const val CLIENTS = "clients"
        const val INVENTORY = "inventory"

        //title of DATABASE date
        const val USERNAME = "username"
        const val DEBTS_TOTAL = "debts"
        const val NUMBER = "number"
        const val NUMBER_DEBTS = "numberDebts"
        const val VALUE_TOTAL = "valueTotal"
        const val AMOUNT = "amount"

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

    fun addInventory(data: Vector<String>, context:Context){

        database.child(INVENTORY).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val inventory = Inventory(
                    amount = data[0].toFloat(),
                    valueBase = data[1].toFloat(),
                    provider = data[2],
                    amountMin = data[3].toFloat(),
                    flete = data[4],
                    name = data[5]
                )
                database.child(INVENTORY).push().setValue(inventory)
                Toast.makeText(context, "Datos agregados con exito", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Ocurrio un error al guardar los datos", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun delDatabase(context: Context){

        val childEventListener = object  : ChildEventListener{
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                database.child(INVENTORY).child(dataSnapshot.key.toString()).removeValue()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Toast.makeText(context, "cambio", Toast.LENGTH_LONG).show()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                //Toast.makeText(context, "removio", Toast.LENGTH_LONG).show()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Toast.makeText(context, "movio", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "cancelar", Toast.LENGTH_LONG).show()
            }
        }
        for(i in 1..10000){
            database.child(INVENTORY).limitToFirst(100).addChildEventListener(childEventListener)
        }

    }

}

data class Inventory(val amount: Float, val provider: String, val valueBase: Float, val name:String, val amountMin:Float, val flete:String)

data class Debts( val debts: Float, val dateTime: LocalDateTime, val amount: Int, val valueUnit: Float, val valueTotal: Float, val key: String? = null)

data class CashSale(val amount: Float, val valueUnit: Float)

data class CreditSale(val amount: Float, val valueUnit: Float, val debut: Float)

data class User(val username: String? = null, val number: String? = null, val numberDebts: Float? = 0f, val debts: Float? = 0f, val key:String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
