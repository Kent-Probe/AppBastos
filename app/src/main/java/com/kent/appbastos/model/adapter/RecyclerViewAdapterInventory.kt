package com.kent.appbastos.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.Inventory
import com.kent.appbastos.model.util.CallBackInventory

class RecyclerViewAdapterInventory(private val values: List<Inventory>, private val eventCallBack: CallBackInventory):
    RecyclerView.Adapter<RecyclerViewAdapterInventory.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val amount: TextView
        val amountMin: TextView
        val flete: TextView
        val name: TextView
        val provider: TextView
        val valueBase: TextView

        init {
            amount = view.findViewById(R.id.amountTextView)
            amountMin = view.findViewById(R.id.amountMinTextView)
            flete = view.findViewById(R.id.fleteTextView)
            name = view.findViewById(R.id.nameTextView)
            provider = view.findViewById(R.id.providerTextView)
            valueBase = view.findViewById(R.id.valueBaseTextView)
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
        holder.amount.text = concatenate("Cantidad: ", inventory.amount.toString())
        holder.amountMin.text = concatenate("Cantidad minima: ", inventory.amountMin.toString())
        holder.flete.text = concatenate("Flete: ", inventory.flete)
        holder.name.text = concatenate("Nombre: ", inventory.name)
        holder.provider.text = concatenate("Provedor: ", inventory.provider)
        holder.valueBase.text = concatenate("Valor base: ", inventory.valueBase.toString())


        holder.itemView.setOnClickListener {
            eventCallBack.onSuccess(inventory)
        }
    }

    override fun getItemCount() = values.size
}