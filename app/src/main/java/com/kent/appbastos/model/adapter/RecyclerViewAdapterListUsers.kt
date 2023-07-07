package com.kent.appbastos.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.model.util.EventCallBackSuccess
import com.kent.appbastos.model.firebase.User

class RecyclerViewAdapterListUsers(private val values:List<User>, private val eventCallBackSuccess: EventCallBackSuccess):
    RecyclerView.Adapter<RecyclerViewAdapterListUsers.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_content, parent, false)

        return ViewHolder(view)
    }

    private fun concatenate(text: String, text2: String):String {
        return text + text2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = values[position]
        holder.nameUser.text = user.username
        holder.numberUser.text = user.number
        holder.amountDebts.text = concatenate("Cantidad: ", user.numberDebts?.toInt().toString())
        holder.debts.text = concatenate("Total: ", user.debts.toString())

        holder.itemView.setOnClickListener {
            eventCallBackSuccess.onSuccess(user)
        }

        holder.itemView.setOnLongClickListener {
            val temp = holder.fatherContainer.visibility
            if(temp == View.VISIBLE){
                holder.fatherContainer.visibility = View.GONE
            }else{
                holder.fatherContainer.visibility = View.VISIBLE
            }
            true
        }

    }

    override fun getItemCount() = values.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameUser: TextView
        val numberUser: TextView
        val debts: TextView
        val amountDebts: TextView
        val fatherContainer: LinearLayoutCompat

        init {
            nameUser = view.findViewById(R.id.nameTextView)
            numberUser = view.findViewById(R.id.numberTextView)
            debts = view.findViewById(R.id.debtsTotal)
            amountDebts = view.findViewById(R.id.amountDebts)
            fatherContainer = view.findViewById(R.id.linerFather)
        }
    }

}