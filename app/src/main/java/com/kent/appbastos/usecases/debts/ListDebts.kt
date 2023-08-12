package com.kent.appbastos.usecases.debts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
import com.kent.appbastos.model.util.BasicEventCallback
import com.kent.appbastos.model.util.CallBackDebts
import com.kent.appbastos.model.util.Keys
import java.util.*

class ListDebts : AppCompatActivity() {

    private val listDebts: MutableList<Debts> = ArrayList()
    private lateinit var btnGeneralSub: Button
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_debts)
        val key = intent.extras?.getString(Keys.KEY).toString()

        //recycler view
        val listDebtsRecyclerView: RecyclerView = findViewById(R.id.listDebtsId)

        //val button
        val floatBtnExit: FloatingActionButton = findViewById(R.id.exitBtn)
        btnGeneralSub = findViewById(R.id.btnGeneralSub)

        //val text view
        val txtDebtsPresents: TextView = findViewById(R.id.txtDebtsPresents)

        floatBtnExit.setOnClickListener {
            finish()
        }
        btnGeneralSub.setOnClickListener {
            DataBaseShareData().readLastDebt(key, object : BasicEventCallback {
                override fun onSuccess(dataSnapshot: DataSnapshot) {
                    val listDebtsTemp: MutableList<Debts> = ArrayList()
                    val listDates: MutableList<Date> = ArrayList()
                    dataSnapshot.children.forEach { child ->
                        val debts = Debts(
                            debts = child.child(Keys.DEBTS).value.toString().toFloat(),
                            dateTime = DateTime(
                                day = child.child(Keys.DATE_TIME).child(Keys.DAY).value.toString()
                                    .toInt(),
                                month = child.child(Keys.DATE_TIME)
                                    .child(Keys.MONTH).value.toString().toInt(),
                                year = child.child(Keys.DATE_TIME).child(Keys.YEAR).value.toString()
                                    .toInt(),
                                hour = child.child(Keys.DATE_TIME).child(Keys.HOUR).value.toString()
                                    .toInt(),
                                minute = child.child(Keys.DATE_TIME)
                                    .child(Keys.MINUTE).value.toString().toInt(),
                                second = child.child(Keys.DATE_TIME)
                                    .child(Keys.SECOND).value.toString().toInt(),
                                milliSecond = child.child(Keys.DATE_TIME)
                                    .child(Keys.MILLISECOND).value.toString().toInt()
                            ),
                            amount = child.child(Keys.AMOUNT).value.toString().toFloat(),
                            valueUnit = child.child(Keys.VALUE_UNIT).value.toString().toFloat(),
                            valueTotal = child.child(Keys.VALUE_TOTAL).value.toString().toFloat(),
                            inventoryOfDebts = InventoryOfDebts(
                                category = child.child(Keys.INVENTORY_OF_DEBTS)
                                    .child(Keys.CATEGORY).value.toString(),
                                flete = child.child(Keys.INVENTORY_OF_DEBTS)
                                    .child(Keys.FLETE).value.toString(),
                                name = child.child(Keys.INVENTORY_OF_DEBTS)
                                    .child(Keys.NAME).value.toString(),
                                provider = child.child(Keys.INVENTORY_OF_DEBTS)
                                    .child(Keys.PROVIDER).value.toString(),
                                key = child.child(Keys.INVENTORY_OF_DEBTS)
                                    .child(Keys.KEY).value.toString(),
                            ),
                            key = child.key
                        )
                        val dateTime = debts.dateTime
                        val calender = Calendar.getInstance()
                        calender.set(
                            dateTime.year,
                            dateTime.month - 1,
                            dateTime.day,
                            dateTime.hour,
                            dateTime.minute,
                            dateTime.second
                        )
                        val date = calender.time
                        date.let {
                            listDates.add(it)
                        }
                        debts.let {
                            listDebtsTemp.add(debts)
                        }
                    }

                    val date = listDates.minOrNull()
                    if (date == null) {
                        Toast.makeText(context, "Error en la fecha $date", Toast.LENGTH_LONG).show()
                    }
                    val debt: Debts? = listDebtsTemp.find {
                        val dateTime = it.dateTime
                        val calender = Calendar.getInstance()
                        calender.set(
                            dateTime.year,
                            dateTime.month - 1,
                            dateTime.day,
                            dateTime.hour,
                            dateTime.minute,
                            dateTime.second
                        )
                        val dateBaseData: Date? = calender.time
                        Toast.makeText(
                            context,
                            "${dateBaseData.toString().equals(date.toString(), true)}",
                            Toast.LENGTH_LONG
                        ).show()
                        dateBaseData.toString().equals(date.toString(), true)
                    }
                    if (debt == null) {
                        Toast.makeText(context, "Error en la deuda $debt", Toast.LENGTH_LONG).show()
                    }

                    val intent = Intent().apply {
                        putExtra(Keys.VALUE_TOTAL, debt?.valueTotal)
                        putExtra(Keys.KEY_DEBTS, debt?.key)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun databaseFailure() {
                    TODO("Not yet implemented")
                }

            })

        }

        try {
            listDebts.clear()
            setupRecyclerView(listDebtsRecyclerView, this, key, txtDebtsPresents)
        }catch (e: Exception){
            Toast.makeText(context, "error ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, context: Context, key: String, txtDebtsPresents: TextView) {
        DataBaseShareData().database.child(Keys.DEBTS).child(key)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listDebts.clear()
                    snapshot.children.forEach { child ->
                        try{
                            val debts = Debts(
                                debts = child.child(Keys.DEBTS).value.toString().toFloat(),
                                dateTime = DateTime(
                                    day = child.child(Keys.DATE_TIME).child(Keys.DAY).value.toString()
                                        .toInt(),
                                    month = child.child(Keys.DATE_TIME)
                                        .child(Keys.MONTH).value.toString().toInt(),
                                    year = child.child(Keys.DATE_TIME).child(Keys.YEAR).value.toString()
                                        .toInt(),
                                    hour = child.child(Keys.DATE_TIME).child(Keys.HOUR).value.toString()
                                        .toInt(),
                                    minute = child.child(Keys.DATE_TIME)
                                        .child(Keys.MINUTE).value.toString().toInt(),
                                    second = child.child(Keys.DATE_TIME)
                                        .child(Keys.SECOND).value.toString().toInt(),
                                    milliSecond = child.child(Keys.DATE_TIME)
                                        .child(Keys.MILLISECOND).value.toString().toInt()
                                ),
                                amount = child.child(Keys.AMOUNT).value.toString().toFloat(),
                                valueUnit = child.child(Keys.VALUE_UNIT).value.toString().toFloat(),
                                valueTotal = child.child(Keys.VALUE_TOTAL).value.toString().toFloat(),
                                inventoryOfDebts = InventoryOfDebts(
                                    category = child.child(Keys.INVENTORY_OF_DEBTS)
                                        .child(Keys.CATEGORY).value.toString(),
                                    flete = child.child(Keys.INVENTORY_OF_DEBTS)
                                        .child(Keys.FLETE).value.toString(),
                                    name = child.child(Keys.INVENTORY_OF_DEBTS)
                                        .child(Keys.NAME).value.toString(),
                                    provider = child.child(Keys.INVENTORY_OF_DEBTS)
                                        .child(Keys.PROVIDER).value.toString(),
                                    key = child.child(Keys.INVENTORY_OF_DEBTS)
                                        .child(Keys.KEY).value.toString(),
                                ),
                                key = child.key
                            )
                            debts.let { listDebts.add(debts) }
                        }catch (e: Exception){
                            Toast.makeText(context, "error: ${e.message}\nfather: ${key}", Toast.LENGTH_LONG).show()
                        }
                    }
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter =
                        RecyclerViewAdapterDebts(listDebts, object : CallBackDebts {
                            override fun onSuccess(debts: Debts) {
                                val intent = Intent().apply {
                                    putExtra(Keys.VALUE_TOTAL, debts.valueTotal)
                                    putExtra(Keys.KEY_DEBTS, debts.key)
                                }
                                setResult(RESULT_OK, intent)
                                finish()
                            }

                        })
                    if (listDebts.size == 0) {
                        recyclerView.visibility = View.GONE
                        txtDebtsPresents.visibility = View.VISIBLE
                        txtDebtsPresents.text = Keys.WITHOUT_DEBTS
                    }

                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }
}