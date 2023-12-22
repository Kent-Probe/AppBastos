package com.kent.appbastos.model.alerts

import android.content.Context
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import com.kent.appbastos.R
import com.kent.appbastos.databinding.DialogDateYearMontBinding
import com.kent.appbastos.model.util.BtnFractions
import com.kent.appbastos.model.util.EventButtonsCallBack
import com.kent.appbastos.model.values.Month


class Alerts {

    interface UtilsOnlyYear{
        fun clickBtnOk(yearSelect: Int)
    }
    interface UtilsAdvance{
        fun clickBtnOk(yearSelect: Int, monthSelect: String)

    }

    fun showAlert(title:String, message:String, positiveButton:String, context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton, null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showAlertDateYear(layoutInflater: LayoutInflater, utils: UtilsOnlyYear){
        val binding = DialogDateYearMontBinding.inflate(layoutInflater)
        val dialogAlert = AlertDialog.Builder(layoutInflater.context)
        dialogAlert.setView(binding.root)

        val dialog = dialogAlert.create()
        dialog.show()

        val currentDate = Calendar.getInstance().get(Calendar.YEAR)

        binding.btnSelectYear.text = currentDate.toString()

        binding.pickerNumber.minValue = 2022
        binding.pickerNumber.maxValue = currentDate + 3
        binding.pickerNumber.value = currentDate
        binding.pickerNumber.setFormatter { String.format("%04d", it) }

        binding.btnCancel.setOnClickListener { dialog.dismiss() }
        binding.btnOk.setOnClickListener {
            utils.clickBtnOk(binding.pickerNumber.value)
            dialog.dismiss()
        }

        binding.pickerNumber.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.btnSelectYear.text = newVal.toString()
        }
    }


    fun showAlertDateYearMonth(layoutInflater: LayoutInflater, utilsAdvance: UtilsAdvance){
        val binding = DialogDateYearMontBinding.inflate(layoutInflater)
        val dialogAlert = AlertDialog.Builder(layoutInflater.context)
        dialogAlert.setView(binding.root)

        binding.btnSelectMonth.visibility = Button.VISIBLE
        binding.btnSelectYear.textSize = 20f
        binding.btnSelectYear.bottom = 0
        val layoutParams = binding.btnSelectYear.layoutParams
        layoutParams.height = dpToPx(layoutInflater, 30f)
        layoutParams.width = dpToPx(layoutInflater, 60f)
        (layoutParams as MarginLayoutParams).setMargins(0, dpToPx(layoutInflater, 2f), 0, 0)
        binding.btnSelectYear.setPadding(
            dpToPx(layoutInflater, 1f))
        binding.btnSelectYear.layoutParams = layoutParams

        val dialog = dialogAlert.create()
        dialog.show()

        val currentDate = Calendar.getInstance().get(Calendar.YEAR)

        binding.btnSelectYear.text = currentDate.toString()

        binding.pickerNumber.minValue = 2022
        binding.pickerNumber.maxValue = currentDate + 3
        binding.pickerNumber.value = currentDate
        binding.pickerNumber.setFormatter { String.format("%04d", it) }

        val month = Month()
        month.initMonth()
        val namesMonth = month.monthName()

        binding.btnSelectMonth.text = month.months[0].month

        binding.pickerMonth.maxValue = month.months.size - 1
        binding.pickerMonth.minValue = 0
        binding.pickerMonth.displayedValues = namesMonth.toArray(arrayOfNulls<String>(namesMonth.size))


        binding.btnSelectYear.setOnClickListener {
            binding.pickerNumber.visibility = NumberPicker.VISIBLE
            binding.pickerMonth.visibility = NumberPicker.GONE
        }
        binding.btnSelectMonth.setOnClickListener {
            binding.pickerNumber.visibility = NumberPicker.GONE
            binding.pickerMonth.visibility = NumberPicker.VISIBLE
        }
        binding.btnCancel.setOnClickListener { dialog.dismiss() }
        binding.btnOk.setOnClickListener {
            utilsAdvance.clickBtnOk(
                binding.pickerNumber.value,
                month.months[binding.pickerMonth.value].month
            )
            dialog.dismiss()
        }

        binding.pickerMonth.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.btnSelectMonth.text = month.months[newVal].month
        }
        binding.pickerNumber.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.btnSelectYear.text = newVal.toString()
        }
    }

    private fun dpToPx(layoutInflater: LayoutInflater, dp: Float): Int {
        return (dp * layoutInflater.context.resources.displayMetrics.density).toInt()
    }


    fun showAlertSelection(layoutInflater: LayoutInflater, context: Context, txtDialog: String, txtBtnUp: String, txtBtnDown: String, eventButtons: EventButtonsCallBack){
        val dialogAlertDialog = AlertDialog.Builder(context)
        val view: View = layoutInflater.inflate(R.layout.dialog_custom_selection, null)

        dialogAlertDialog.setView(view)

        val alertDialog = dialogAlertDialog.create()
        alertDialog.show()

        val btnUp: Button = view.findViewById(R.id.btnUp)
        btnUp.text = txtBtnUp
        val btnDown: Button = view.findViewById(R.id.btnDown)
        btnDown.text = txtBtnDown
        val textDialog: TextView = view.findViewById(R.id.textDialog)
        textDialog.text = txtDialog

        btnUp.setOnClickListener{
            eventButtons.buttonUp(alertDialog)
        }

        btnDown.setOnClickListener {
            eventButtons.buttonDown(alertDialog)
        }
    }

    fun showAlertSelection(layoutInflater: LayoutInflater, context: Context, txtDialog: String, txtBtn: String, btnFractions: BtnFractions){
        val dialogAlert = AlertDialog.Builder(context)
        val view: View = layoutInflater.inflate(R.layout.dialog_custom, null)

        dialogAlert.setView(view)

        val alertDialog = dialogAlert.create()
        alertDialog.show()

        //txt Title and text button
        view.findViewById<TextView>(R.id.textDialog).text = txtDialog
        view.findViewById<Button>(R.id.btnCancel).text = txtBtn

        //Buttons
        view.findViewById<Button>(R.id.btn1).setOnClickListener { btnFractions.btn1(alertDialog) }

        view.findViewById<Button>(R.id.btn2).setOnClickListener { btnFractions.btn2(alertDialog) }

        view.findViewById<Button>(R.id.btn3).setOnClickListener { btnFractions.btn3(alertDialog) }

        view.findViewById<Button>(R.id.btn4).setOnClickListener { btnFractions.btn4(alertDialog) }

        view.findViewById<Button>(R.id.btnCancel).setOnClickListener { btnFractions.btnCancel(alertDialog) }

    }


}

