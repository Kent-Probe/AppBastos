package com.kent.appbastos.usecases.dashboard

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kent.appbastos.R
import com.kent.appbastos.databinding.ActivityInfoDashboardBinding
import com.kent.appbastos.databinding.BottomSheetFilterForDateBinding
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.alerts.DatePickerFragment
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.util.OnChange

class InfoDashboard : AppCompatActivity() {
    //Bottom Sheet Dialog
    private lateinit var bottomSheetDialog: BottomSheetDialog

    //Toolbar of the screen
    private lateinit var toolbar: Toolbar

    private lateinit var binding: ActivityInfoDashboardBinding
    private lateinit var bindingBottomSheet: BottomSheetFilterForDateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
                }
            })
            else if (bindingBottomSheet.spinnerFilter.selectedItem.equals(options[1])) Alerts().showAlertDateYearMonth(layoutInflater, object : Alerts.UtilsAdvance{
                override fun clickBtnOk(yearSelect: Int, monthSelect: String) {
                    bindingBottomSheet.datePickerSearch.hint = ""
                    bindingBottomSheet.fatherTextDate.hint = ""
                    bindingBottomSheet.datePickerSearch.text = Editable.Factory.getInstance().newEditable(
                        "$monthSelect del $yearSelect"
                    )
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
            val intent = Intent(this@InfoDashboard, ListLog::class.java)
            startActivity(intent)
        }

        bindingBottomSheet.btnApplyFilter.setOnClickListener {
            val filtre = bindingBottomSheet.datePickerSearch.text.toString()
            val extraIntent = if(filtre.equals("Fecha...", true)) "0" else filtre
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
        onChange.onChange(textDate)
    }
}