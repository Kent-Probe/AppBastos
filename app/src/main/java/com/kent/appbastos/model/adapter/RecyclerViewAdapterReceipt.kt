package com.kent.appbastos.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.Receipt
import com.kent.appbastos.model.util.CallBackReceipt
import com.kent.appbastos.model.util.Keys

class RecyclerViewAdapterReceipt(private val values: List<Receipt>, private val eventCallBack: CallBackReceipt):
    RecyclerView.Adapter<RecyclerViewAdapterReceipt.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView
        val nameClient: TextView
        val numberClient: TextView
        val date: TextView
        val nameProduct: TextView
        val category: TextView
        val amount: TextView
        val valueUnit: TextView
        val valueTotal: TextView

        init {
            title = view.findViewById(R.id.txtTitle)
            nameClient = view.findViewById(R.id.nameClient)
            numberClient = view.findViewById(R.id.numberClient)
            date = view.findViewById(R.id.date)
            nameProduct = view.findViewById(R.id.nameProduct)
            category = view.findViewById(R.id.category)
            amount = view.findViewById(R.id.amount)
            valueUnit = view.findViewById(R.id.valueUnit)
            valueTotal = view.findViewById(R.id.valueTotal)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.receipt_content, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val receipt = values[position]

        holder.title.text = Keys.FORMAT_RECEIPT_REFERENCE.format(receipt.reference)
        holder.nameClient.text = Keys.FORMAT_RECEIPT_CLIENT.format(receipt.nameClient)
        if(receipt.number != "null"){
            holder.numberClient.text = Keys.FORMAT_RECEIPT_NUMBER.format(receipt.number)
            holder.numberClient.visibility = TextView.VISIBLE
        }else{
            holder.numberClient.visibility = TextView.GONE
        }
        holder.date.text = Keys.FORMAT_RECEIPT_DATE_TIME.format(receipt.dateTime)
        holder.nameProduct.text = Keys.FORMAT_RECEIPT_TYPE.format(receipt.product)
        holder.category.text = Keys.FORMAT_RECEIPT_CATEGORY.format(receipt.category)
        holder.amount.text = Keys.FORMAT_RECEIPT_AMOUNT.format(receipt.amount)
        holder.valueUnit.text = Keys.FORMAT_RECEIPT_VALUE_UNIT.format(receipt.valueUnit)
        holder.valueTotal.text = Keys.FORMAT_RECEIPT_VALUE_TOTAL.format(receipt.valueTotal)

        holder.itemView.setOnClickListener {
            eventCallBack.onSuccess(receipt)
        }
    }
    override fun getItemCount() = values.size
}