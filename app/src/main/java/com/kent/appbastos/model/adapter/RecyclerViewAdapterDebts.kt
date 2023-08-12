package com.kent.appbastos.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.Debts
import com.kent.appbastos.model.util.CallBackDebts
import com.kent.appbastos.model.util.Keys

class RecyclerViewAdapterDebts(private val values: List<Debts>, private val eventCallBack: CallBackDebts):
    RecyclerView.Adapter<RecyclerViewAdapterDebts.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val debts: TextView
        val valueUnit: TextView
        val amount: TextView
        val date: TextView
        val category: TextView
        val name: TextView

        init {
            debts = view.findViewById(R.id.txtDebts)
            valueUnit = view.findViewById(R.id.txtValueUnit)
            amount = view.findViewById(R.id.txtAmount)
            date = view.findViewById(R.id.txtDate)
            category = view.findViewById(R.id.txtCategory)
            name = view.findViewById(R.id.txtNameInventory)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.debts_content, parent, false)

        return ViewHolder(view)
    }

    private fun concatenate(text: String, text2: String):String {
        return text + text2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val debts = values[position]
        val monthName: String = getMonthName(debts.dateTime.month)
        val hour = if (debts.dateTime.hour.toString().length == 1){
            "0" + debts.dateTime.hour
        }else{
            debts.dateTime.hour.toString()
        }
        val second = if(debts.dateTime.second.toString().length == 1){
            "0" + debts.dateTime.second
        }else{
            debts.dateTime.second.toString()
        }

        holder.debts.text = concatenate("Total: ", Keys.FORMAT_PRICE.format(debts.valueTotal))
        holder.valueUnit.text = concatenate("Valor por unidad: ", Keys.FORMAT_PRICE.format(debts.valueUnit))
        holder.amount.text = concatenate("Cantidad: ", Keys.FORMAT_AMOUNT.format(debts.amount))
        holder.category.text = concatenate("categoria: ", debts.inventoryOfDebts.category)
        holder.name.text = concatenate("Tipo: ", debts.inventoryOfDebts.name)
        holder.date.text = Keys.FORMAT_DATE_STRING.format(debts.dateTime.day, monthName, debts.dateTime.year, hour, second)


        holder.itemView.setOnClickListener {
            eventCallBack.onSuccess(debts)
        }
    }

    override fun getItemCount() = values.size

    private fun getMonthName(monthNumber: Int): String {
        return when (monthNumber) {
            1 -> "Enero"
            2 -> "Febrero"
            3 -> "Marzo"
            4 -> "Abril"
            5 -> "Mayo"
            6 -> "Junio"
            7 -> "Julio"
            8 -> "Agosto"
            9 -> "Septiembre"
            10 -> "Octubre"
            11 -> "Noviembre"
            12 -> "Diciembre"
            else -> "El número de mes debe estar entre 1 y 12."
        }
    }
}