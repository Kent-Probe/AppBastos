package com.kent.appbastos.usecases.debts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kent.appbastos.R
import com.kent.appbastos.model.adapter.RecyclerViewAdapterDebts
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.DateTime
import com.kent.appbastos.model.firebase.Debts
import com.kent.appbastos.model.firebase.InventoryOfDebts
import com.kent.appbastos.model.util.CallBackDebts
import com.kent.appbastos.model.util.Keys

class ListDebts : AppCompatActivity() {

    private val listDebts:MutableList<Debts> = ArrayList()
    private val database = DataBaseShareData().database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_debts)
        val key = intent.extras?.getString(Keys.KEY).toString()

        //recycler view
        val listDebtsRecyclerView: RecyclerView = findViewById(R.id.listDebtsId)

        //val button
        val floatBtnExit: FloatingActionButton = findViewById(R.id.exitBtn)

        //val text view
        val txtDebtsPresents: TextView = findViewById(R.id.txtDebtsPresents)

        floatBtnExit.setOnClickListener {
            finish()
        }

        listDebts.clear()
        setupRecyclerView(listDebtsRecyclerView, this, key, txtDebtsPresents)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, context: Context, key: String, txtDebtsPresents: TextView){
        val messagesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listDebts.clear()
                snapshot.children.forEach { child ->
                    val debts = Debts(
                        debts = child.child(Keys.DEBTS).value.toString().toFloat(),
                        dateTime = DateTime(
                            day = child.child(Keys.DATE_TIME).child(Keys.DAY).value.toString().toInt(),
                            month = child.child(Keys.DATE_TIME).child(Keys.MONTH).value.toString().toInt(),
                            year = child.child(Keys.DATE_TIME).child(Keys.YEAR).value.toString().toInt(),
                        ),
                        amount = child.child(Keys.AMOUNT).value.toString().toInt(),
                        valueUnit = child.child(Keys.VALUE_UNIT).value.toString().toFloat(),
                        valueTotal = child.child(Keys.VALUE_TOTAL).value.toString().toFloat(),
                        inventoryOfDebts = InventoryOfDebts(
                            category = child.child(Keys.INVENTORY_OF_DEBTS).child(Keys.CATEGORY).value.toString(),
                            flete = child.child(Keys.INVENTORY_OF_DEBTS).child(Keys.FLETE).value.toString(),
                            name = child.child(Keys.INVENTORY_OF_DEBTS).child(Keys.NAME).value.toString(),
                            provider = child.child(Keys.INVENTORY_OF_DEBTS).child(Keys.PROVIDER).value.toString(),
                            key = child.child(Keys.INVENTORY_OF_DEBTS).child(Keys.KEY).value.toString(),
                        ),
                        key = child.key
                    )
                    debts.let { listDebts.add(debts) }
                }
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = RecyclerViewAdapterDebts(listDebts, object : CallBackDebts {
                    override fun onSuccess(debts: Debts) {
                        val intent = Intent().apply {
                            putExtra(Keys.VALUE_TOTAL, debts.valueTotal)
                            putExtra(Keys.KEY_DEBTS, debts.key)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                })
                if(listDebts.size == 0){
                    recyclerView.visibility = View.GONE
                    txtDebtsPresents.visibility = View.VISIBLE
                    txtDebtsPresents.text = Keys.WITHOUT_DEBTS
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        database.child(Keys.DEBTS).child(key).addListenerForSingleValueEvent(messagesListener)

    }
}