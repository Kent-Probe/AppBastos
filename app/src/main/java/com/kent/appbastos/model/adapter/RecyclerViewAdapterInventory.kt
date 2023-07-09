package com.kent.appbastos.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.Inventory
import com.kent.appbastos.model.util.CallBackInventory
import com.kent.appbastos.model.util.Keys

class RecyclerViewAdapterInventory(private val values: List<Inventory>, private val eventCallBack: CallBackInventory):
    RecyclerView.Adapter<RecyclerViewAdapterInventory.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val amount: TextView
        val amountMin: TextView
        val flete: TextView
        val name: TextView
        val provider: TextView
        val valueBase: TextView
        val category: TextView
        val date: TextView

        init {
            amount = view.findViewById(R.id.amountTextView)
            amountMin = view.findViewById(R.id.amountMinTextView)
            flete = view.findViewById(R.id.fleteTextView)
            name = view.findViewById(R.id.nameTextView)
            provider = view.findViewById(R.id.providerTextView)
            valueBase = view.findViewById(R.id.valueBaseTextView)
            category = view.findViewById(R.id.categoryTextView)
            date = view.findViewById(R.id.dateTextView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inventory_content, parent, false)

        return ViewHolder(view)
    }

    private fun concatenate(text: String, text2: String):String {
        return text + text2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val inventory = values[position]
        holder.amount.text = concatenate("Cantidad: ", Keys.FORMAT_AMOUNT.format(inventory.amount))
        holder.amountMin.text = concatenate("Cantidad minima: ", Keys.FORMAT_AMOUNT.format(inventory.amountMin))
        holder.flete.text = concatenate("Flete: ", inventory.flete)
        holder.name.text = concatenate("Nombre: ", inventory.name)
        holder.category.text = concatenate("Categoria: ", inventory.category)
        holder.provider.text = concatenate("Provedor: ", inventory.provider)
        holder.valueBase.text = concatenate("Valor base: ", Keys.FORMAT_PRICE.format(inventory.valueBase))
        holder.date.text = Keys.FORMAT_DATE.format(inventory.date.day, getMonthName(inventory.date.month), inventory.date.year)


        holder.itemView.setOnClickListener {
            eventCallBack.onSuccess(inventory)
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