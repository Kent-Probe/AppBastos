package com.kent.appbastos.usecases.inventory.listInventory
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kent.appbastos.R
import com.kent.appbastos.model.adapter.RecyclerViewAdapterInventory
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.DateTime
import com.kent.appbastos.model.firebase.Inventory
import com.kent.appbastos.model.util.CallBackInventory
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.usecases.inventory.addInventory.AddInventory
import com.kent.appbastos.usecases.sale.CashSale
import com.kent.appbastos.usecases.sale.CreditSale

class ListInventory : AppCompatActivity() {

    private val listInventory:MutableList<Inventory> = ArrayList()
    private val database = DataBaseShareData().database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_inventory)

        val titleScreen = intent.extras?.getString(Keys.TITLE)

        //Data share
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val rol = pref.getString(Keys.ROL, null).toString()

        //fill recycle view
        val listInventoryRecyclerView: RecyclerView = findViewById(R.id.listInventory)

        //Val button
        val floatButtonBack: FloatingActionButton = findViewById(R.id.exitBtn)
        val floatBtnAddInventory: FloatingActionButton = findViewById(R.id.btnAddInventory)

        if(rol != Keys.ROL_ADMIN){
            floatBtnAddInventory.visibility = Button.GONE
        }else{
            floatBtnAddInventory.visibility = Button.VISIBLE
        }

        floatBtnAddInventory.setOnClickListener {
            val intent = Intent(this, AddInventory::class.java)
            startActivity(intent)
        }

        floatButtonBack.setOnClickListener {
            finish()
        }

        listInventory.clear()
        setupRecyclerView(this, listInventoryRecyclerView, titleScreen)

    }

    //Function of Recycler View with Database
    private fun setupRecyclerView(context: Context, recyclerView: RecyclerView, titleScreen: String?) {

        val messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listInventory.clear()
                dataSnapshot.children.forEach { child ->
                    val key: String? = child.key
                    val inventory =
                        Inventory(
                            amount = child.child(Keys.AMOUNT).value.toString().toFloat(),
                            provider = child.child(Keys.PROVIDER).value.toString(),
                            valueBase = child.child(Keys.VALUE_BASE).value.toString().toFloat(),
                            name = child.child(Keys.NAME).value.toString(),
                            amountMin = child.child(Keys.AMOUNT_MIN).value.toString().toFloat(),
                            flete = child.child(Keys.FLETE).value.toString(),
                            category = child.child(Keys.CATEGORY).value.toString(),
                            date = DateTime(
                                day = child.child(Keys.DATE_TIME).child(Keys.DAY).value.toString().toInt(),
                                month = child.child(Keys.DATE_TIME).child(Keys.MONTH).value.toString().toInt(),
                                year = child.child(Keys.DATE_TIME).child(Keys.YEAR).value.toString().toInt(),
                                hour = child.child(Keys.DATE_TIME).child(Keys.HOUR).value.toString().toInt(),
                                minute = child.child(Keys.DATE_TIME).child(Keys.MINUTE).value.toString().toInt(),
                                second = child.child(Keys.DATE_TIME).child(Keys.SECOND).value.toString().toInt(),
                                milliSecond = child.child(Keys.DATE_TIME).child(Keys.MILLISECOND).value.toString().toInt()
                            ),
                            key = key,
                        )
                    inventory.let {
                        if(it.amount != 0f) listInventory.add(inventory)
                    }
                }
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = RecyclerViewAdapterInventory(listInventory, object :
                    CallBackInventory {
                    override fun onSuccess(inventory: Inventory) {
                        if (!titleScreen.isNullOrEmpty()) {
                            if(titleScreen == Keys.CREDIT_SALE){
                                val intent = Intent(context, CreditSale::class.java).apply {
                                    putExtra(Keys.KEY, inventory.key)
                                    putExtra(Keys.NAME, inventory.name)
                                    putExtra(Keys.PROVIDER, inventory.provider)
                                    putExtra(Keys.CATEGORY, inventory.category)
                                    putExtra(Keys.VALUE_BASE, inventory.valueBase)
                                    putExtra(Keys.AMOUNT, inventory.amount)
                                    putExtra(Keys.AMOUNT_MIN, inventory.amountMin)
                                    putExtra(Keys.FLETE, inventory.flete)
                                }
                                startActivity(intent)
                                finish()
                            }else {
                                val intent = Intent(context, CashSale::class.java).apply {
                                    putExtra(Keys.KEY, inventory.key)
                                    putExtra(Keys.NAME, inventory.name)
                                    putExtra(Keys.PROVIDER, inventory.provider)
                                    putExtra(Keys.CATEGORY, inventory.category)
                                    putExtra(Keys.VALUE_BASE, inventory.valueBase)
                                    putExtra(Keys.AMOUNT, inventory.amount)
                                    putExtra(Keys.AMOUNT_MIN, inventory.amountMin)
                                    putExtra(Keys.FLETE, inventory.flete)
                                }
                                startActivity(intent)
                                finish()
                            }
                        }
                        Toast.makeText(context, String.format(Keys.TOAST_MSM_INVENTORY, inventory.name), Toast.LENGTH_SHORT).show()
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: EN LISTINVENTORY (REVISAR)")
            }
        }
        database.child(Keys.INVENTORY).addListenerForSingleValueEvent(messagesListener)
    }
}



