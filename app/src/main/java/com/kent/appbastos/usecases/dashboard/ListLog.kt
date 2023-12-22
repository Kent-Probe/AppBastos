package com.kent.appbastos.usecases.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.kent.appbastos.databinding.ActivityListLogBinding
import com.kent.appbastos.model.adapter.AdapterListLogSale
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.values.LogSale
import com.kent.appbastos.usecases.receipt.Receipt

class ListLog : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var binding: ActivityListLogBinding

    private val listLogSale = listOf(
        LogSale("1", "$200.000", "2", "$100.000"),
        LogSale("2", "$200.000", "2", "$100.000"),
        LogSale("3", "$200.000", "2", "$100.000"),
        LogSale("4", "$200.000", "2", "$100.000")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filter = intent.extras?.getString(Keys.EXTRA, Keys.ERROR)

        initRecyclerView()

        val title = "Lista de registros %s"
        //Assign toolbar and config it
        toolbar = binding.toolbarLayout.toolbar
        toolbar.title = title.format("hoy")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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