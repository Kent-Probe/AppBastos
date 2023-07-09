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

class DataBaseShareData () {

    val database = Firebase.database.reference

    private fun increment(valueS: String, increment: Float): Float{
        return valueS.toFloat() + increment
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

    //Write new cash sale
    fun writeNewCashSale(){
        val cashSale = CashSale(0f, 0f)
        database.child(Keys.CASH_SALE).setValue(cashSale)
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

        database.child(Keys.INVENTORY).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val inventory = Inventory(
                    amount = data[0].toFloat(),
                    valueBase = data[1].toFloat(),
                    provider = data[2],
                    amountMin = data[3].toFloat(),
                    flete = data[4],
                    category = data[5],
                    name = data[6],
                    date = DateTime(day, month, year)
                )
                database.child(Keys.INVENTORY).push().setValue(inventory)
                Toast.makeText(context, Keys.TOAST_ADD_SUCCESSFULLY, Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, Keys.TOAST_ERROR_SAVE_DATA, Toast.LENGTH_LONG).show()
            }

        })
    }

}

data class DateTime(val day: Int, val month: Int, val year: Int)

data class Inventory(val amount: Float, val provider: String, val valueBase: Float, val name:String, val amountMin:Float, val flete:String, val category: String, val date: DateTime, val key:String? = null)

data class InventoryOfDebts(val provider: String, val name:String, val flete:String, val category: String, val key:String? = null)

data class Debts(val debts: Float, val dateTime: DateTime, val amount: Int, val valueUnit: Float, val valueTotal: Float, val inventoryOfDebts: InventoryOfDebts, val key: String? = null)

data class CashSale(val amount: Float, val valueUnit: Float)

//data class CreditSaleFirebase(val amount: Float, val valueUnit: Float, val debut: Float)

data class UserApp(val firstName: String, val lastName: String, val email: String, val rol: String, val key: String? = null)

data class User(val username: String? = null, val number: String? = null, val numberDebts: Float? = 0f, val debts: Float? = 0f, val key:String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
