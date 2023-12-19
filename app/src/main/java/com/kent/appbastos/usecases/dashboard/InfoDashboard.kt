package com.kent.appbastos.usecases.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.kent.appbastos.R
import com.kent.appbastos.model.alerts.Alerts

class InfoDashboard : AppCompatActivity() {
    //Bottom Sheet Dialog
    private lateinit var bottomSheetDialog: BottomSheetDialog

    //Toolbar of the screen
    private lateinit var toolbar: Toolbar

    //Val spinner
    private lateinit var sipnner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_dashboard)

        //assign toolbar, and config it
        toolbar = findViewById(R.id.toolbar_layout)
        toolbar.title = "Dashboard"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    //Create option for the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details_client, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.filter -> {
                buttonSheetShow()
            }
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buttonSheetShow() {
        //open bottom sheet show
        val bottomDialogView = layoutInflater.inflate(R.layout.bottom_sheet_filter_for_date, null)
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomDialogView)
        bottomSheetDialog.show()

        //configure spinner
        sipnner = bottomDialogView.findViewById(R.id.spinnerFilter)
        //Options
        val options = arrayOf("Solo por año", "por mes y año", "por mes, año y dia")
        //Adapter
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options)
        //Adapter with the spinner
        sipnner.adapter = adapter

        bottomDialogView.findViewById<TextInputEditText>(R.id.datePickerSearch).setOnClickListener {
            Alerts().showAlertDateForMont(layoutInflater, this)
        }
    }
}