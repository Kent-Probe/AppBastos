package com.kent.appbastos.model.firebase

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kent.appbastos.model.util.BasicEventCallback
import java.util.*

class DataBaseShareData () {

    val database = Firebase.database.reference

    companion object{
        //title of DATABASE
        const val DEBTS = "debts"
        const val USERS = "users"
        const val USERS_APP = "usersApp"
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
    }
    private fun increment(valueS: String, increment: Float): Float{
        return valueS.toFloat() + increment
    }

    //Write payment
    fun addPayment(nameClient: String, key: String, valueTotal: Float){
        database.child(DEBTS).child(nameClient).child(key).child(VALUE_TOTAL).setValue(valueTotal)
    }

    //Write new debts
    fun writeDebts(context: Context, debts: Debts, nameClient: String, keyInventory: String, newAmount: String){
        database.child(INVENTORY).child(keyInventory).child("amount").setValue(newAmount.toFloat())

        database.child(DEBTS).child(nameClient).push().setValue(debts)
        checkClientExist(nameClient, object : BasicEventCallback {
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
                Toast.makeText(context, "Error con un dato", Toast.LENGTH_SHORT).show()
            }

            override fun databaseFailure() {
                Toast.makeText(context, "Error con la base da datos", Toast.LENGTH_SHORT).show()
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

    //Write new User App on firebase realtime database
    fun writeNewUserApp(context: Context, userUID: String, userApp: UserApp, callBack: BasicEventCallback){
        val databaseUser = database.child(USERS).child(USERS_APP).child(userUID)
        databaseUser.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    callBack.onSuccess(snapshot)
                    Toast.makeText(context, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show()
                    return
                }
                databaseUser.setValue(userApp).addOnCompleteListener {
                    if(it.isSuccessful){
                        callBack.onSuccess(snapshot)
                        Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callBack.databaseFailure()
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })
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
                    name = data[5],
                    category = data[6]
                )
                database.child(INVENTORY).push().setValue(inventory)
                Toast.makeText(context, "Datos agregados con exito", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Ocurrio un error al guardar los datos", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun checkDebts(type: String, callBack: BasicEventCallback){
        val inventory: Query = database.child(INVENTORY)

        inventory.orderByChild("name").equalTo(type).limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) callBack.onSuccess(snapshot)
                else callBack.onCancel()
            }

            override fun onCancelled(error: DatabaseError) {
                callBack.databaseFailure()
            }

        })
    }

}

data class DateTime(val day: Int, val month: Int, val year: Int)

data class Inventory(val amount: Float, val provider: String, val valueBase: Float, val name:String, val amountMin:Float, val flete:String, val category: String, val key:String? = null)

data class Debts(val debts: Float, val dateTime: DateTime, val amount: Int, val valueUnit: Float, val valueTotal: Float, val key: String? = null)

data class CashSale(val amount: Float, val valueUnit: Float)

data class CreditSaleFirebase(val amount: Float, val valueUnit: Float, val debut: Float)

data class UserApp(val firstName: String, val lastName: String, val email: String, val rol: String, val key: String? = null)

data class User(val username: String? = null, val number: String? = null, val numberDebts: Float? = 0f, val debts: Float? = 0f, val key:String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
