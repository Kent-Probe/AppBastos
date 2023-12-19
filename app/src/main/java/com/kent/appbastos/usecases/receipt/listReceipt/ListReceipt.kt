package com.kent.appbastos.usecases.receipt.listReceipt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kent.appbastos.R
import com.kent.appbastos.model.adapter.RecyclerViewAdapterReceipt
import com.kent.appbastos.model.alerts.DatePickerFragment
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.Receipt
import com.kent.appbastos.model.util.CallBackReceipt
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.model.util.OnChange
import com.kent.appbastos.usecases.receipt.ReceiptOpenActivity

class ListReceipt : AppCompatActivity() {

    //val list, recycle view and response
    private val listReceipt: MutableList<Receipt> = ArrayList()
    private lateinit var listReceiptRecyclerView: RecyclerView
    private lateinit var response: AppCompatTextView

    //val for the menu in toolbar
    private lateinit var toolbar: Toolbar
    private lateinit var dateSelect: TextView
    private lateinit var fatherDateSelect: TextInputLayout

    //val in the toolbar
    private lateinit var itemSearchView: SearchView
    private lateinit var itemSearchIcon: MenuItem

    //Bottom Sheet Dialog
    private lateinit var bottomSheetDialog: BottomSheetDialog


    //val util
    private val context: Context = this

    //add filter
    private lateinit var add: FilterAdd
    enum class FilterAdd{
        ALL,
        CREDIT,
        CASH
    }

    //Type
    enum class TypeFilter {
        NONE,
        REFERENCE,
        NAME_CLIENT,
        NUMBER,
        DATE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_receipt)
        //filter Add default
        add = FilterAdd.ALL

        //variable recycle view and response text view
        listReceiptRecyclerView = findViewById(R.id.listReceipts)
        response = findViewById(R.id.response)

        //fill recycler view
        listReceipt.clear()
        setupRecyclerView(this, TypeFilter.NONE, add)

        //assign toolbar, textView of dateSelect and his father, and config
        toolbar = findViewById(R.id.toolbar)
        dateSelect = findViewById(R.id.datePickerSearch)
        fatherDateSelect = findViewById(R.id.fatherTextDate)

        //to config Search fot the date and his father
        dateSelect.hint = "Fecha..."
        fatherDateSelect.visibility = TextInputEditText.GONE

        //to config toolbar
        toolbar.title = "Recibos"
        setSupportActionBar(toolbar)

        // Habilitar la flecha de atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Onclick and more event's for the dataSelect
        dateSelect.setOnClickListener {
            showDatePickerDialog(object : OnChange{
                override fun onChange(text: String?) {
                    dateSelect.text = text
                    fatherDateSelect.hint = ""
                    dateSelect.hint = ""
                }
            })
        }
    }

    private fun setupRecyclerView(context: Context, filter: TypeFilter, filterAdd: FilterAdd, filterSearch: String? = null) {
        //config response and recycle  view
        listReceiptRecyclerView.visibility = RecyclerView.GONE
        response.visibility = AppCompatTextView.VISIBLE
        response.text = Keys.MSM_LOADING

        val messagesListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listReceipt.clear()
                if((filterAdd == FilterAdd.CASH || filterAdd == FilterAdd.ALL) && filter != TypeFilter.NUMBER){
                    snapshot.child(Keys.CASH_SALE).children.forEach {
                        val receipt =
                            Receipt(
                                reference = it.child(Keys.REFERENCE).value.toString(),
                                dateTime = it.child(Keys.DATE_TIME).value.toString(),
                                nameClient = it.child(Keys.NAME_CLIENT).value.toString(),
                                product = it.child(Keys.PRODUCT).value.toString(),
                                category = it.child(Keys.CATEGORY).value.toString(),
                                valueUnit = it.child(Keys.VALUE_UNIT).value.toString(),
                                amount = it.child(Keys.AMOUNT).value.toString(),
                                valueTotal = it.child(Keys.VALUE_TOTAL).value.toString(),
                                number = "null",
                                key = it.key,
                                keyInventory = it.child(Keys.KEY_INVENTORY).value.toString()
                            )
                        //filter data
                        when(filter){
                            TypeFilter.REFERENCE -> {
                                if(receipt.reference.contains(filterSearch.toString(), true)){
                                    receipt.let { rec ->  listReceipt.add(rec) }
                                }
                            }
                            TypeFilter.NAME_CLIENT -> {
                                if(receipt.nameClient.contains(filterSearch.toString(), true)){
                                    receipt.let { rec -> listReceipt.add(rec) }
                                }
                            }
                            TypeFilter.DATE -> {
                                //all after "," with ","
                                val regex = ",.*".toRegex()

                                val receiptDate = receipt.dateTime.replace(regex, "")
                                val date = filterSearch?.replace(regex, "")

                                if(receiptDate == date){
                                    receipt.let { rec ->  listReceipt.add(rec) }
                                }
                            }
                            else -> {
                                receipt.let { rec ->  listReceipt.add(rec) }
                            }
                        }
                    }
                }
                if(filterAdd == FilterAdd.CREDIT || filterAdd == FilterAdd.ALL){
                    snapshot.child(Keys.CREDIT_SALE).children.forEach {
                        val receipt =
                            Receipt(
                                reference = it.child(Keys.REFERENCE).value.toString(),
                                dateTime = it.child(Keys.DATE_TIME).value.toString(),
                                nameClient = it.child(Keys.NAME_CLIENT).value.toString(),
                                product = it.child(Keys.PRODUCT).value.toString(),
                                category = it.child(Keys.CATEGORY).value.toString(),
                                valueUnit = it.child(Keys.VALUE_UNIT).value.toString(),
                                amount = it.child(Keys.AMOUNT).value.toString(),
                                valueTotal = it.child(Keys.VALUE_TOTAL).value.toString(),
                                number = it.child(Keys.NUMBER).value.toString(),
                                key = it.key,
                                keyInventory = it.child(Keys.KEY_INVENTORY).value.toString()
                            )
                        //Filter result
                        when(filter){
                            TypeFilter.REFERENCE -> {
                                if(receipt.reference.contains(filterSearch.toString(), true)){
                                    receipt.let { rec ->  listReceipt.add(rec) }
                                }
                            }
                            TypeFilter.NAME_CLIENT -> {
                                if(receipt.nameClient.contains(filterSearch.toString(), true)){
                                    receipt.let { rec -> listReceipt.add(rec) }
                                }
                            }
                            TypeFilter.NUMBER -> {
                                if(receipt.number?.replace(" ", "")!!.contains(filterSearch.toString(), true)){
                                    receipt.let { rec -> listReceipt.add(rec) }
                                }
                            }
                            TypeFilter.DATE -> {
                                //all after "," with ","
                                val regex = "[,.*]".toRegex()

                                val receiptDate = receipt.dateTime.replace(regex, "")
                                val date = filterSearch?.replace(regex, "")

                                if(receiptDate == date){
                                    receipt.let { rec ->  listReceipt.add(rec) }
                                }
                            }
                            else -> {
                                receipt.let { rec ->  listReceipt.add(rec) }
                            }
                        }
                    }
                }

                listReceiptRecyclerView.layoutManager = LinearLayoutManager(context)
                listReceiptRecyclerView.adapter = RecyclerViewAdapterReceipt(listReceipt, object : CallBackReceipt{
                    override fun onSuccess(receipt: Receipt) {
                        val intent = Intent(context, ReceiptOpenActivity::class.java).apply {
                            putExtra(Keys.KEY_INVENTORY, receipt.keyInventory)
                            putExtra(Keys.INVENTORY, receipt.amount)
                        }
                        startActivity(intent)
                    }

                })
                if(listReceipt.isEmpty()){
                    response.text = Keys.WITHOUT
                }else{
                    listReceiptRecyclerView.visibility = RecyclerView.VISIBLE
                    response.visibility = TextView.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "revise su conexion a internet", Toast.LENGTH_LONG).show()
            }

        }

        DataBaseShareData().readAllReceipt(messagesListener)
    }

    //Function for show dialog date picker
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

        val dateFormat = Keys.FORMAT_DATE.format(day, Keys.getMonthName(month + 1), year, 0, 0)

        //fill recycler view
        listReceipt.clear()
        setupRecyclerView(this, TypeFilter.DATE, add, dateFormat)
    }

    //Create option for the Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        itemSearchIcon = menu!!.findItem(R.id.search)
        itemSearchView = itemSearchIcon.actionView as SearchView
        return super.onCreateOptionsMenu(menu)
    }

    //Fun eject when select on item of menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search -> {
                //select item search
                dateSelect.text = ""
                fatherDateSelect.visibility = TextInputEditText.GONE
                itemSearchView.queryHint = "Referencia..."
                itemSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        //fill recycler view
                        listReceipt.clear()
                        setupRecyclerView(context, TypeFilter.REFERENCE, add, query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if(newText.isNullOrEmpty()){
                            listReceipt.clear()
                            setupRecyclerView(context, TypeFilter.NONE, add)
                        }
                        return true
                    }

                })
            }
            //select filter
            R.id.filter -> {
                bottomSheetShow()
                itemSearchIcon.collapseActionView()

            }
            else -> return false
        }
        return true
    }

    private fun bottomSheetShow() {
        //val's output bottom sheet
        fatherDateSelect.visibility = TextInputLayout.GONE
        itemSearchIcon.expandActionView() //collapse view en el toolbar
        itemSearchView.inputType = InputType.TYPE_CLASS_TEXT

        itemSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //fill recycler view
                listReceipt.clear()
                setupRecyclerView(context, TypeFilter.REFERENCE, add, query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    listReceipt.clear()
                    setupRecyclerView(context, TypeFilter.NONE, add)
                }
                return true
            }

        })

        //open bottom sheet show
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()

        //radio button
        val radioGroup = bottomSheetDialog.findViewById<RadioGroup>(R.id.radioGroup)

        //Val of search
        val btnDatePicker = bottomSheetDialog.findViewById<TextView>(R.id.datePickerSearch)
        val fatherBtnDatePicker = bottomSheetDialog.findViewById<TextInputLayout>(R.id.fatherTextDate)
        val spinnerSelectionSale = bottomSheetDialog.findViewById<Spinner>(R.id.spinnerSale)
        val btnApply = bottomSheetDialog.findViewById<AppCompatButton>(R.id.btnApply)

        //configure spinner
        //Options
        val options = arrayOf("Toods", "venta a contado", "venta a credito")
        //Adapter
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options)
        //Adapter with the spinner
        spinnerSelectionSale?.adapter = adapter
        when(add){
            FilterAdd.ALL -> spinnerSelectionSale?.setSelection(0)
            FilterAdd.CASH -> spinnerSelectionSale?.setSelection(1)
            FilterAdd.CREDIT -> spinnerSelectionSale?.setSelection(2)
        }


        radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            itemSearchView.inputType = InputType.TYPE_CLASS_TEXT

            //Val's into bottom sheet dialog
            fatherBtnDatePicker?.visibility = TextInputLayout.GONE
            btnApply?.text = getString(R.string.code_apply)
            btnDatePicker?.text = null
            btnDatePicker?.hint = "Fecha..."

            //Val's output bottom sheet dialog
            fatherDateSelect.visibility = TextInputLayout.GONE

            when(checkedId){
                R.id.rdoFilterRef ->{
                    itemSearchView.queryHint = "Referencia..."
                    itemSearchIcon.expandActionView()


                    itemSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            //fill recycler view
                            listReceipt.clear()
                            setupRecyclerView(context, TypeFilter.REFERENCE, add, query)
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if(newText.isNullOrEmpty()){
                                listReceipt.clear()
                                setupRecyclerView(context, TypeFilter.NONE, add)
                            }
                            return true
                        }

                    })


                }
                R.id.rdoFilterClient ->{
                    itemSearchView.queryHint = "Nombre del cliente..."
                    itemSearchIcon.expandActionView()

                    itemSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            //fill recycler view
                            listReceipt.clear()
                            setupRecyclerView(context, TypeFilter.NAME_CLIENT, add, query)
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if(newText.isNullOrEmpty()){
                                listReceipt.clear()
                                setupRecyclerView(context, TypeFilter.NONE, add)
                            }
                            return true
                        }

                    })
                }
                R.id.rdoFilterNumber ->{
                    itemSearchView.queryHint = "Numero del cliente..."
                    itemSearchView.inputType = InputType.TYPE_CLASS_NUMBER
                    itemSearchIcon.expandActionView()

                    itemSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            //fill recycler view
                            listReceipt.clear()
                            setupRecyclerView(context, TypeFilter.NUMBER, add, query)
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if(newText.isNullOrEmpty()){
                                listReceipt.clear()
                                setupRecyclerView(context, TypeFilter.NONE, add)
                            }
                            return true
                        }

                    })
                }
                R.id.rdoFilterDate ->{
                    //collapse search view
                    itemSearchIcon.collapseActionView()

                    fatherBtnDatePicker?.visibility = TextInputLayout.VISIBLE
                    btnApply?.text = getString(R.string.code_search)

                    btnDatePicker?.setOnClickListener {
                        showDatePickerDialog(object : OnChange{
                            override fun onChange(text: String?) {
                                //Val's int bottom Sheet dialog
                                btnDatePicker.text = text
                                fatherBtnDatePicker?.hint = ""
                                btnDatePicker.hint = ""

                                //Val's output bottom sheet dialog
                                dateSelect.text = text
                                fatherDateSelect.hint = ""
                                dateSelect.hint = ""
                            }

                        })
                    }

                    //Val's output bottom sheet dialog
                    fatherDateSelect.visibility = TextInputLayout.VISIBLE
                }
                else -> {
                    return@setOnCheckedChangeListener
                }
            }
        }

        btnApply?.setOnClickListener {
            add = when(spinnerSelectionSale?.selectedItemPosition){
                1 -> {
                    FilterAdd.CASH
                }
                2 -> {
                    FilterAdd.CREDIT
                }
                else->{
                    FilterAdd.ALL
                }
            }
            setupRecyclerView(context, TypeFilter.NONE, add)
            bottomSheetDialog.dismiss()
        }
    }
}