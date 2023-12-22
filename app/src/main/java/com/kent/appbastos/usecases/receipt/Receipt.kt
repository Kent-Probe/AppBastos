package com.kent.appbastos.usecases.receipt

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.R
import com.kent.appbastos.databinding.ActivityReceiptBinding
import com.kent.appbastos.model.firebase.*
import com.kent.appbastos.model.firebase.Receipt
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.usecases.share.Share


class Receipt : AppCompatActivity() {

    private lateinit var binding: ActivityReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Tittle
        val title = intent.extras?.getString(Keys.TITLE, Keys.ERROR).toString()

        //Variable button
        binding.exitBtn.setOnClickListener {
            finish()
        }


        if(title.equals("ListLog", true)) {
            binding.btnContinue.visibility = Button.GONE
            return
        }
        //Current Date
        val calendar = Calendar.getInstance()
        val now = DateTime(
            day = calendar.get(Calendar.DAY_OF_MONTH),
            month = calendar.get(Calendar.MONTH) + 1,
            year = calendar.get(Calendar.YEAR),
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            second = calendar.get(Calendar.SECOND),
            milliSecond = calendar.get(Calendar.MILLISECOND)
        )

        //data of the cash sale or credit sale
        val data :MutableMap<String, Any>
        if(title == Keys.CASH_SALE){
            data = hashMapOf(
                Keys.TITLE to title,
                Keys.NAME_CLIENT to intent.extras?.getString(Keys.NAME_CLIENT, Keys.TEMP).toString(),
                Keys.CATEGORY to intent.extras?.getString(Keys.CATEGORY, Keys.ERROR).toString(),
                Keys.VALUE_UNIT to intent.extras?.getFloat(Keys.VALUE_UNIT, 0f).toString(),
                Keys.AMOUNT to intent.extras?.getFloat(Keys.AMOUNT, 0f).toString(),
                Keys.TYPE to intent.extras?.getString(Keys.TYPE, Keys.ERROR).toString(),
                Keys.DATE_TIME to now
            )
        }else{
            data = hashMapOf(
                Keys.TITLE to title,
                Keys.NAME_CLIENT to intent.extras?.getString(Keys.NAME_CLIENT, Keys.TEMP).toString(),
                Keys.CATEGORY to intent.extras?.getString(Keys.CATEGORY, Keys.ERROR).toString(),
                Keys.VALUE_UNIT to intent.extras?.getFloat(Keys.VALUE_UNIT, 0f).toString(),
                Keys.AMOUNT to intent.extras?.getFloat(Keys.AMOUNT, 0f).toString(),
                Keys.TYPE to intent.extras?.getString(Keys.TYPE, Keys.ERROR).toString(),
                Keys.NUMBER_CLIENT to intent.extras?.getString(Keys.NUMBER_CLIENT, Keys.ERROR).toString(),
                Keys.DATE_TIME to now
            )
        }

        if (title == Keys.CASH_SALE) {
            //assign title
            findViewById<TextView>(R.id.title).text = Keys.FORMAT_TITLE_RECEIPT.format("contado")
        } else {
            //assign title
            findViewById<TextView>(R.id.title).text = Keys.FORMAT_TITLE_RECEIPT.format("credito")
            findViewById<TextInputLayout>(R.id.inputNumberClient).visibility = TextInputLayout.VISIBLE
            findViewById<TextView>(R.id.numberClient).text = data[Keys.NUMBER_CLIENT].toString()
        }

        //data of inventory
        val inventory :MutableMap<String, Any> = hashMapOf(
            Keys.KEY_INVENTORY to intent.extras?.getString(Keys.KEY_INVENTORY, Keys.ERROR).toString(),
            Keys.AMOUNT_INVENTORY to intent.extras?.getFloat(Keys.AMOUNT_INVENTORY, 0f).toString(),
            Keys.CATEGORY to intent.extras?.getString(Keys.CATEGORY, Keys.ERROR).toString(),
            Keys.PROVIDER to intent.extras?.getString(Keys.PROVIDER, Keys.ERROR).toString(),
            Keys.NAME to intent.extras?.getString(Keys.TYPE, Keys.ERROR).toString(),
            Keys.FLETE to intent.extras?.getString(Keys.FLETE, Keys.ERROR).toString()
        )

        //Data in receipt
        //reference
        val uid = now.day.toString() + now.month.toString() + now.year.toString() + "-" + (0..999).random()
        val reference = "D${uid}"
        //Data
        val totalNum = data[Keys.VALUE_UNIT].toString().toFloat() * data[Keys.AMOUNT].toString().toFloat()
        val valueUnit = Keys.FORMAT_PRICE.format(data[Keys.VALUE_UNIT].toString().toFloat())
        val amount = Keys.FORMAT_AMOUNT_FRACTION.format(floatToFraction(intent.extras?.getFloat(Keys.AMOUNT, 0f).toString()))
        val total = Keys.FORMAT_PRICE.format(totalNum)
        val date = Keys.FORMAT_DATE.format(now.day, Keys.getMonthName(now.month), now.year, now.hour, now.minute)
        val category = data[Keys.CATEGORY].toString()
        val nameProduct = data[Keys.TYPE].toString()
        val nameClient = data[Keys.NAME_CLIENT].toString()
        val numberClient = data[Keys.NUMBER_CLIENT].toString()

        //variables of the screen
        findViewById<TextInputEditText>(R.id.nameClient).setText(nameClient)
        findViewById<TextInputEditText>(R.id.dateTime).setText(date)
        findViewById<TextInputEditText>(R.id.product).setText(nameProduct)
        findViewById<TextInputEditText>(R.id.category).setText(category)
        findViewById<TextInputEditText>(R.id.amount).setText(amount)
        findViewById<TextInputEditText>(R.id.valueUnit).setText(valueUnit)
        findViewById<TextInputEditText>(R.id.valueTotal).setText(total)
        findViewById<TextInputEditText>(R.id.reference).setText(reference)

        findViewById<Button>(R.id.btnContinue).setOnClickListener {
            //Save data in the database
            val debts = Debts(
                debts = totalNum,
                dateTime = now,
                amount = data[Keys.AMOUNT].toString().toFloat(),
                valueUnit = data[Keys.VALUE_UNIT].toString().toFloat(),
                valueTotal = totalNum,
                inventoryOfDebts = InventoryOfDebts(
                    category = inventory[Keys.CATEGORY].toString(),
                    name = inventory[Keys.NAME].toString(),
                    provider = inventory[Keys.PROVIDER].toString(),
                    flete = inventory[Keys.FLETE].toString(),
                    key = inventory[Keys.KEY_INVENTORY].toString()
                )
            )
            val father = if(title == Keys.CASH_SALE) Keys.CASH_SALE else data[Keys.NAME_CLIENT].toString()
            DataBaseShareData().writeDebts(this, debts, father, inventory[Keys.KEY_INVENTORY].toString(), inventory[Keys.AMOUNT_INVENTORY].toString())

            Toast.makeText(this, Keys.TOAST_ADD_SUCCESSFULLY, Toast.LENGTH_LONG).show()

            val intent = Intent(this, Share::class.java).apply {
                putExtra(Keys.DATE_TIME, date)
                putExtra(Keys.NAME_CLIENT, nameClient)
                putExtra(Keys.TYPE, nameProduct)
                putExtra(Keys.CATEGORY, category)
                putExtra(Keys.VALUE_TOTAL, total)
                putExtra(Keys.AMOUNT, amount)
                putExtra(Keys.VALUE_UNIT, valueUnit)
                putExtra(Keys.REFERENCE, reference)
                putExtra(Keys.NUMBER_CLIENT, numberClient)
            }

            val number = Keys.formatNumber(numberClient)

            val receipt = Receipt(
                reference,
                date,
                nameClient,
                nameProduct,
                category,
                valueUnit,
                amount,
                total,
                number,
                null,
                inventory[Keys.KEY_INVENTORY].toString()
            )
            DataBaseShareData().writeReceipt(title, receipt)

            startActivity(intent)
            finish()
        }
    }

    private fun floatToFraction(str: String?): String {
        if(str.isNullOrEmpty()) return  "error con la cantidad"

        return when(str) {
            "0.5" -> "1/2"
            "0.75" -> "3/4"
            "0.25" -> "1/4"
            "0.125" -> "1/8"
            else -> str.split(".")[0]
        }
    }


}

