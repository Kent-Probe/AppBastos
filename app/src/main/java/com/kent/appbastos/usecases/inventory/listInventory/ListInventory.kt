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
import com.kent.appbastos.model.firebase.Inventory
import com.kent.appbastos.model.util.CallBackInventory
import com.kent.appbastos.usecases.inventory.addInventory.AddInventory
import com.kent.appbastos.usecases.mainPrincipal.MainMenu
import com.kent.appbastos.usecases.sale.CreditSale

class ListInventory : AppCompatActivity() {

    private val listInventory:MutableList<Inventory> = ArrayList()
    private val database = DataBaseShareData().database

    companion object{
        const val INVENTORY = "inventory"
        const val AMOUNT = "amount"
        const val AMOUNT_MIN = "amountMin"
        const val FLETE = "flete"
        const val NAME = "name"
        const val PROVIDER = "provider"
        const val VALUE_BASE = "valueBase"
        const val CATEGORY = "category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_inventory)

        val creditSale = intent.extras?.getBoolean("creditSale")

        //Data share
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val rol = pref.getString(MainMenu.ROL, null).toString()

        //fill recycle view
        val listInventoryRecyclerView: RecyclerView = findViewById(R.id.listInventory)

        //Val button
        val floatButtonBack: FloatingActionButton = findViewById(R.id.exitBtn)
        val floatBtnAddInventory: FloatingActionButton = findViewById(R.id.btnAddInventory)

        if(rol != "admin"){
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
        setupRecyclerView(this, listInventoryRecyclerView, creditSale)

    }

    //Function of Recycler View with Database
    private fun setupRecyclerView(context: Context, recyclerView: RecyclerView, creditSale: Boolean?) {

        val messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listInventory.clear()
                dataSnapshot.children.forEach { child ->
                    val key: String? = child.key
                    val inventory =
                        Inventory(
                            amount = child.child(AMOUNT).value.toString().toFloat(),
                            provider = child.child(PROVIDER).value.toString(),
                            valueBase = child.child(VALUE_BASE).value.toString().toFloat(),
                            name = child.child(NAME).value.toString(),
                            amountMin = child.child(AMOUNT_MIN).value.toString().toFloat(),
                            flete = child.child(FLETE).value.toString(),
                            category = child.child(CATEGORY).value.toString(),
                            key = key,
                        )
                    inventory.let { listInventory.add(inventory) }
                }
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = RecyclerViewAdapterInventory(listInventory, object :
                    CallBackInventory {
                    override fun onSuccess(inventory: Inventory) {
                        if (creditSale == true) {
                            val intent = Intent(context, CreditSale::class.java).apply {
                                putExtra("key", inventory.key)
                                putExtra(NAME, inventory.name)
                                putExtra(PROVIDER, inventory.provider)
                                putExtra(CATEGORY, inventory.category)
                                putExtra(VALUE_BASE, inventory.valueBase)
                                putExtra(AMOUNT, inventory.amount)
                                putExtra(AMOUNT_MIN, inventory.amountMin)
                            }
                            startActivity(intent)
                            finish()
                        }
                        Toast.makeText(context, "Inventaio de ${inventory.name}", Toast.LENGTH_SHORT).show()
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled:")
            }
        }
        database.child(INVENTORY).addListenerForSingleValueEvent(messagesListener)
    }
}



