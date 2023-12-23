package com.kent.appbastos.usecases.dashboard

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kent.appbastos.R
import com.kent.appbastos.databinding.ActivityInfoDashboardBinding
import com.kent.appbastos.databinding.BottomSheetFilterForDateBinding
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.alerts.DatePickerFragment
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.util.OnChange
import java.util.*

class InfoDashboard : AppCompatActivity() {
    //Bottom Sheet Dialog
    private lateinit var bottomSheetDialog: BottomSheetDialog

    //Toolbar of the screen
    private lateinit var toolbar: Toolbar

    private var key: String = "-1"
    private var month: String = "-1"

    private lateinit var binding: ActivityInfoDashboardBinding
    private lateinit var bindingBottomSheet: BottomSheetFilterForDateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DataBaseShareData().readDashboard(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val amountMonth = snapshot.child(Keys.AMOUNT_SALES_MONTH).value.toString()
                val amountDay = snapshot.child(Keys.AMOUNT_SALES_TODAY).value.toString()
                val inputMonth = snapshot.child(Keys.INPUT_MONTH).value.toString().toInt()
                val inputDay = snapshot.child(Keys.INPUT_DAY).value.toString().toInt()
                binding.amountMonth.text = amountMonth
                binding.amountDay.text = amountDay
                binding.inputDay.text = Keys.FORMAT_PRICE_REGULAR.format(inputDay)
                binding.inputMonth.text = Keys.FORMAT_PRICE_REGULAR.format(inputMonth)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR:", error.message)
                Toast.makeText(this@InfoDashboard, "Ocurrio un error, revise conexion a internet", Toast.LENGTH_SHORT).show()
                finish()
            }

        })

        //assign toolbar, and config it
        toolbar = binding.toolbarLayout.toolbar
        toolbar.title = "Dashboard"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //button log today
        binding.btnShowLog.setOnClickListener {
            buttonSheetShow()
        }
    }

    //Create option for the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details_client, menu)
        menu?.findItem(R.id.filter)?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    private fun buttonSheetShow() {
        bindingBottomSheet = BottomSheetFilterForDateBinding.inflate(layoutInflater)
        //open bottom sheet show
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bindingBottomSheet.root)
        bottomSheetDialog.show()

        //configure spinner
        //Options
        val options = arrayOf("Solo por año", "por mes y año", "por mes, año y dia")
        //Adapter
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options)
        //Adapter with the spinner
        bindingBottomSheet.spinnerFilter.adapter = adapter

        bindingBottomSheet.datePickerSearch.setOnClickListener {
            bindingBottomSheet.datePickerSearch.hint = ""
            bindingBottomSheet.fatherTextDate.hint = ""
            bindingBottomSheet.datePickerSearch.text = Editable.Factory.getInstance().newEditable("Fecha...")

            if(bindingBottomSheet.spinnerFilter.selectedItem.equals(options[0])) Alerts().showAlertDateYear(layoutInflater, object : Alerts.UtilsOnlyYear{
                override fun clickBtnOk(yearSelect: Int) {
                    bindingBottomSheet.datePickerSearch.hint = ""
                    bindingBottomSheet.fatherTextDate.hint = ""
                    bindingBottomSheet.datePickerSearch.text = Editable.Factory.getInstance().newEditable(yearSelect.toString())
                    key = yearSelect.toString()
                }
            })
            else if (bindingBottomSheet.spinnerFilter.selectedItem.equals(options[1])) Alerts().showAlertDateYearMonth(layoutInflater, object : Alerts.UtilsAdvance{
                override fun clickBtnOk(yearSelect: Int, monthSelect: String, month: Int) {
                    val monthS = if(month+1 <= 9) "0${month+1}" else month+1
                    bindingBottomSheet.datePickerSearch.hint = ""
                    bindingBottomSheet.fatherTextDate.hint = ""
                    bindingBottomSheet.datePickerSearch.text = Editable.Factory.getInstance().newEditable(
                        "$monthSelect del $yearSelect"
                    )
                    key = "${yearSelect}$monthS"
                }
            })
            else
                showDatePickerDialog(object : OnChange{
                override fun onChange(text: String?) {
                    bindingBottomSheet.datePickerSearch.hint = ""
                    bindingBottomSheet.fatherTextDate.hint = ""
                    bindingBottomSheet.datePickerSearch.text = Editable.Factory.getInstance().newEditable(text)
                }
            })
        }

        bindingBottomSheet.btnClearFilter.setOnClickListener {
            bindingBottomSheet.datePickerSearch.text = null
            bindingBottomSheet.datePickerSearch.hint = "Fecha..."
        }

        bindingBottomSheet.showLog.setOnClickListener {
            val current = Calendar.getInstance()
            val filter = "${current.get(Calendar.YEAR)}${current.get(Calendar.MONTH)+1}${current.get(Calendar.DAY_OF_MONTH)}"
            val intent = Intent(this@InfoDashboard, ListLog::class.java).apply {
                putExtra(Keys.EXTRA, filter)
            }
            startActivity(intent)
        }

        bindingBottomSheet.btnApplyFilter.setOnClickListener {
            val filter = bindingBottomSheet.datePickerSearch.text.toString()
            val extraIntent = if(filter.equals("Fecha...", true)) "0" else key
            val intent = Intent(this@InfoDashboard, ListLog::class.java).apply {
                putExtra(Keys.EXTRA, extraIntent)
            }
            startActivity(intent)
        }
    }

    private fun showDatePickerDialog(changes: OnChange){
        val datePicker = DatePickerFragment { day, month, year ->
            onDateSelected(day, month, year, changes)
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    //Function select day, month, year
    private fun onDateSelected(day: Int, month: Int, year: Int, onChange: OnChange){
        val textDate = "$day/$month/$year"

        val dayS = if(day <= 9) "0${day}" else day
        val monthS = if(month+1 <= 9) "0${month+1}" else "${month+1}"

        key = "$year${monthS}$dayS"
        onChange.onChange(textDate)
    }
}