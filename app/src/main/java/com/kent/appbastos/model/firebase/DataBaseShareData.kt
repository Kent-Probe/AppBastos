package com.kent.appbastos.model.firebase

import android.content.Context
import android.icu.util.Calendar
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kent.appbastos.model.util.BasicEventCallback
import com.kent.appbastos.model.util.Keys
import java.util.*

class DataBaseShareData {

    val database = Firebase.database.reference

    private fun increment(valueS: String, increment: Float): Float{
        return valueS.toFloat() + increment
    }

    fun updateRolUserApp(key: String){
        database.child(Keys.USER).child(Keys.USERS_APP).child(key).child(Keys.ROL).setValue(Keys.ROL_ADMIN)
    }

    fun addEventOnlyListenerUserApp(messagesListener: ValueEventListener){
        database.child(Keys.USER).child(Keys.USERS_APP).addListenerForSingleValueEvent(messagesListener)
    }

    //Write payment
    fun addPayment(nameClient: String, key: String, valueTotal: Float){
        if(valueTotal != 0f){
            database.child(Keys.DEBTS).child(nameClient).child(key).child(Keys.VALUE_TOTAL).setValue(valueTotal)
            return
        }
        database.child(Keys.DEBTS).child(nameClient).child(key).setValue(null)
    }

    //Write new debts
    fun writeDebts(context: Context, debts: Debts, nameClient: String, keyInventory: String, newAmount: String){
        database.child(Keys.INVENTORY).child(keyInventory).child(Keys.AMOUNT).setValue(newAmount.toFloat())

        database.child(Keys.DEBTS).child(nameClient).push().setValue(debts)
        checkClientExist(nameClient, object : BasicEventCallback {
            override fun onSuccess(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { child ->
                    val updates: MutableMap<String, Any> = hashMapOf(
                        Keys.DEBTS_TOTAL to increment(child.child(Keys.DEBTS_TOTAL).value.toString(), debts.valueTotal),
                        Keys.NUMBER to child.child(Keys.NUMBER).value.toString(),
                        Keys.NUMBER_DEBTS to increment(child.child(Keys.NUMBER_DEBTS).value.toString(), 1f),
                        Keys.USERNAME to child.child(Keys.USERNAME).value.toString()
                    )
                    database.child(Keys.USER).child(Keys.CLIENTS).child(child.key.toString()).setValue(updates)
                }
            }

            override fun onCancel() {
                Toast.makeText(context, Keys.TOAST_ERROR_SOME_DATA, Toast.LENGTH_SHORT).show()
            }

            override fun databaseFailure() {
                Toast.makeText(context, Keys.TOAST_ERROR_DATABASE, Toast.LENGTH_SHORT).show()
            }

        })
    }

    //Write new user
    fun writeNewUser(name: String, number: String) {
        val user = User(name, number)
        database.child(Keys.USER).child(Keys.CLIENTS).push().setValue(user)
    }

    //Write new User App on firebase realtime database
    fun writeNewUserApp(context: Context, userUID: String, userApp: UserApp, callBack: BasicEventCallback){
        val databaseUser = database.child(Keys.USER).child(Keys.USERS_APP).child(userUID)
        databaseUser.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    callBack.onSuccess(snapshot)
                    Toast.makeText(context, Keys.WELCOME_AGAIN, Toast.LENGTH_SHORT).show()
                    return
                }
                databaseUser.setValue(userApp).addOnCompleteListener {
                    if(it.isSuccessful){
                        callBack.onSuccess(snapshot)
                        Toast.makeText(context, Keys.WELCOME, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, Keys.ERROR, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callBack.databaseFailure()
                Toast.makeText(context, Keys.ERROR, Toast.LENGTH_SHORT).show()
            }
        })
    }

    //Function for check exit user
    fun checkClientExist(nameClient: String, callBack: BasicEventCallback){
        val databaseUser = database.child(Keys.USER).child(Keys.CLIENTS)

        val userByNameClient: Query = databaseUser.orderByChild(Keys.USERNAME).equalTo(nameClient).limitToFirst(1)

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
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val milliSecond = calendar.get(Calendar.MILLISECOND)

        val inventory = Inventory(
            amount = data[0].toFloat(),
            valueBase = data[1].toFloat(),
            provider = data[2],
            amountMin = data[3].toFloat(),
            flete = data[4],
            category = data[5],
            name = data[6],
            date = DateTime(day, month, year, hour, minute, second, milliSecond)
        )
        database.child(Keys.INVENTORY).push().setValue(inventory)
        Toast.makeText(context, Keys.TOAST_ADD_SUCCESSFULLY, Toast.LENGTH_LONG).show()
    }

    fun writeReceipt(title: String, receipt: Receipt){
        database.child(Keys.RECEIPT).child(title).push().setValue(receipt)
    }

    fun readAllReceipt(listener: ValueEventListener){
        database.child(Keys.RECEIPT).addListenerForSingleValueEvent(listener)
    }

    fun readLastDebt(nameClient: String, callBack: BasicEventCallback){
        database.child(Keys.DEBTS).child(nameClient).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                callBack.onSuccess(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                callBack.onCancel()
            }

        })
    }

    fun writeNewAmountInventory(amountNew: Float?, keyReceipt: String?)  {
        database.child(Keys.INVENTORY).child(keyReceipt!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val amountCurrent = snapshot.child(Keys.AMOUNT).value.toString().toFloat()
                val amountTotal = amountCurrent - amountNew!!
                database.child(Keys.INVENTORY).child(keyReceipt).child(Keys.AMOUNT).setValue(amountTotal)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun readDashboard(listener: ValueEventListener){
        database.child(Keys.DASHBOARD).child(Keys.INFO).addListenerForSingleValueEvent(listener)
    }

    fun readLogDateDatabase(key: String, listener: ValueEventListener){
        database.child(Keys.DASHBOARD).child(Keys.LOG).child(key).addListenerForSingleValueEvent(listener)
    }

    fun readLogDatabase(listener: ValueEventListener){
        database.child(Keys.DASHBOARD).child(Keys.LOG).addListenerForSingleValueEvent(listener)
    }

}

data class Receipt(val reference: String, val dateTime: String, val nameClient: String, val product: String, val category: String, val valueUnit: String, val amount: String, val valueTotal: String, val number:String? = null, val key: String? = null, val keyInventory: String)

data class DateTime(val day: Int, val month: Int, val year: Int, val hour: Int, val minute: Int, val second: Int, val milliSecond: Int)

data class Inventory(val amount: Float, val provider: String, val valueBase: Float, val name:String, val amountMin:Float, val flete:String, val category: String, val date: DateTime, val key:String? = null)

data class InventoryOfDebts(val provider: String, val name:String, val flete:String, val category: String, val key:String? = null)

data class Debts(val debts: Float?, val dateTime: DateTime, val amount: Float, val valueUnit: Float, val valueTotal: Float, val inventoryOfDebts: InventoryOfDebts, val key: String? = null)

//data class CreditSaleFirebase(val amount: Float, val valueUnit: Float, val debut: Float)

data class UserApp(val firstName: String, val lastName: String, val email: String, val rol: String, val key: String? = null)

data class User(val username: String? = null, val number: String? = null, val numberDebts: Float? = 0f, val debts: Float? = 0f, val key:String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
