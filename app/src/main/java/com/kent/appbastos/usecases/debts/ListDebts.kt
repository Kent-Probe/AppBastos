package com.kent.appbastos.usecases.debts

import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
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
import com.kent.appbastos.model.CallBackDebts
import com.kent.appbastos.model.adapter.RecyclerViewAdapterDebts
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.DateTime
import com.kent.appbastos.model.firebase.Debts

class ListDebts : AppCompatActivity() {

    companion object {
        //Properties
        const val DEBTS = "debts"
        const val AMOUNT = "amount"
        const val DATE_TIME = "dateTime"
        const val VALUE_UNIT = "valueUnit"
        const val VALUE_TOTAL = "valueTotal"
        const val KEY_DEBTS = "keyDebts"


        val NNDR = "No nay deudas registradas"
    }

    private val listDebts:MutableList<Debts> = ArrayList()
    private val database = DataBaseShareData().database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_debts)
        val key = intent.extras?.getString("key").toString()

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
                val calendar = Calendar.getInstance()
                val year: Int = calendar.get(Calendar.YEAR)
                val month: Int = calendar.get(Calendar.MONTH) + 1
                val day: Int = calendar.get(Calendar.DAY_OF_MONTH)


                listDebts.clear()
                snapshot.children.forEach { child ->
                    val debts = Debts(
                        debts = child.child(DEBTS).value.toString().toFloat(),
                        dateTime = DateTime(day, month, year),
                        amount = child.child(AMOUNT).value.toString().toInt(),
                        valueUnit = child.child(VALUE_UNIT).value.toString().toFloat(),
                        valueTotal = child.child(VALUE_TOTAL).value.toString().toFloat(),
                        key = child.key
                    )
                    debts.let { listDebts.add(debts) }
                }
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = RecyclerViewAdapterDebts(listDebts, object : CallBackDebts {
                    override fun onSuccess(debts: Debts) {
                        val intent = Intent().apply {
                            putExtra(VALUE_TOTAL, debts.valueTotal)
                            putExtra(KEY_DEBTS, debts.key)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                })
                if(listDebts.size == 0){
                    recyclerView.visibility = View.GONE
                    txtDebtsPresents.visibility = View.VISIBLE
                    txtDebtsPresents.text = NNDR
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        database.child(DataBaseShareData.DEBTS).child(key).addListenerForSingleValueEvent(messagesListener)

    }
}