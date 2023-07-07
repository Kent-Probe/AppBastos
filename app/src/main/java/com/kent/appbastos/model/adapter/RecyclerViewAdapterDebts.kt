package com.kent.appbastos.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.model.util.CallBackDebts
import com.kent.appbastos.model.firebase.Debts

class RecyclerViewAdapterDebts(private val values: List<Debts>, private val eventCallBack: CallBackDebts):
    RecyclerView.Adapter<RecyclerViewAdapterDebts.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val debts: TextView
        val valueUnit: TextView
        val amount: TextView

        init {
            debts = view.findViewById(R.id.txtDebts)
            valueUnit = view.findViewById(R.id.txtValueUnit)
            amount = view.findViewById(R.id.txtAmount)
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
        holder.debts.text = concatenate("Total: ", debts.valueTotal.toString())
        holder.valueUnit.text = concatenate("Valor por unidad: ", debts.valueUnit.toString())
        holder.amount.text = concatenate("Cantidad: ", debts.amount.toString())


        holder.itemView.setOnClickListener {
            eventCallBack.onSuccess(debts)
        }
    }

    override fun getItemCount() = values.size
}