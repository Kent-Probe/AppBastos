package com.kent.appbastos.usecases.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kent.appbastos.databinding.ActivityListLogBinding
import com.kent.appbastos.model.adapter.AdapterListLogSale
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.values.LogSale
import com.kent.appbastos.usecases.receipt.Receipt

class ListLog : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var binding: ActivityListLogBinding

    private val listLogSale: ArrayList<LogSale> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filter = intent.extras?.getString(Keys.EXTRA, Keys.ERROR)

        if(filter == null || filter == Keys.ERROR){
            Toast.makeText(this, "ocurrio un error", Toast.LENGTH_LONG).show()
            finish()
        }

        val title = "Lista de registros %s"
        //Assign toolbar and config it
        toolbar = binding.toolbarLayout.toolbar
        toolbar.title = title.format("hoy")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        Toast.makeText(this, filter, Toast.LENGTH_SHORT).show()
        when (filter?.length) {
            8 -> {
                DataBaseShareData().readLogDateDatabase(filter, object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            listLogSale.add(
                                LogSale(
                                    ref = it.child(Keys.REF_LOG).value.toString(),
                                    nameClient = it.child(Keys.NAME_CLIENT).value.toString(),
                                    amount = it.child(Keys.AMOUNT_LOG).value.toString().toInt(),
                                    valueTotal = it.child(Keys.VALUE_TOTAL_LOG).value.toString().toFloat(),
                                    valueUnit = it.child(Keys.VALUE_UNIT_LOG).value.toString().toFloat(),
                                    typeSale = it.child(Keys.VALUE_TYPE_SALE_LOG).value.toString(),
                                    key = it.key
                                ))
                        }
                        initRecyclerView()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("ERROR:", error.message)
                        Toast.makeText(this@ListLog, "Ocurrio un error, verifique su conexion a internet", Toast.LENGTH_LONG).show()
                    }

                })
            }
            6 -> {
                DataBaseShareData().readLogDatabase(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            if (it.key.toString().startsWith(filter)) {
                                Log.i("INFO:", it.children.toString())
                                it.children.forEach { logs ->
                                    Log.i("INFO:", logs.value.toString())
                                    listLogSale.add(LogSale(
                                        ref = logs.child(Keys.REF_LOG).value.toString(),
                                        nameClient = logs.child(Keys.NAME_CLIENT).value.toString(),
                                        amount = logs.child(Keys.AMOUNT_LOG).value.toString().toInt(),
                                        valueTotal = logs.child(Keys.VALUE_TOTAL_LOG).value.toString().toFloat(),
                                        valueUnit = logs.child(Keys.VALUE_UNIT_LOG).value.toString().toFloat(),
                                        typeSale = logs.child(Keys.VALUE_TYPE_SALE_LOG).value.toString(),
                                        key = logs.key
                                    ))
                                }
                            }
                        }
                        initRecyclerView()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("ERROR:", error.message)
                        Toast.makeText(this@ListLog, "Ocurrio un error, verifique su conexion a internet", Toast.LENGTH_LONG).show()
                    }

                })
            }
            4 -> {
                DataBaseShareData().readLogDatabase(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            if (it.key.toString().startsWith(filter)) {
                                Log.i("INFO:", it.children.toString())
                                it.children.forEach { logs ->
                                    Log.i("INFO:", logs.value.toString())
                                    listLogSale.add(LogSale(
                                        ref = logs.child(Keys.REF_LOG).value.toString(),
                                        nameClient = logs.child(Keys.NAME_CLIENT_LOG).value.toString(),
                                        amount = logs.child(Keys.AMOUNT_LOG).value.toString().toInt(),
                                        valueTotal = logs.child(Keys.VALUE_TOTAL_LOG).value.toString().toFloat(),
                                        valueUnit = logs.child(Keys.VALUE_UNIT_LOG).value.toString().toFloat(),
                                        typeSale = logs.child(Keys.VALUE_TYPE_SALE_LOG).value.toString(),
                                        key = logs.key
                                    ))
                                }
                            }
                        }
                        initRecyclerView()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("ERROR:", error.message)
                        Toast.makeText(this@ListLog, "Ocurrio un error, verifique su conexion a internet", Toast.LENGTH_LONG).show()
                    }

                })
            }
            else -> {
                // Manejar el caso por defecto o realizar alguna acción si el length no coincide con ninguno de los casos esperados
            }
        }
    }

    private fun initRecyclerView(){
        binding.rvListLog.layoutManager = LinearLayoutManager(this@ListLog)
        val adapter = AdapterListLogSale(listLogSale, object : AdapterListLogSale.Interaction{
            override fun onClickItem() {
                val intent = Intent(this@ListLog, Receipt::class.java).apply {
                    putExtra(Keys.TITLE, "ListLog")
                }
                startActivity(intent)
            }

        })
        binding.rvListLog.adapter = adapter
    }
}