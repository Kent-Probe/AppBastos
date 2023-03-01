package com.kent.appbastos.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.model.EventCallBackSuccess
import com.kent.appbastos.model.firebase.User

class RecyclerViewAdapter(private val values:List<User>, private val context: Context, private val eventCallBackSuccess: EventCallBackSuccess):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_content, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = values[position]
        holder.nameUser.text = user.username
        holder.numberUser.text = user.number

        holder.itemView.setOnClickListener {
            eventCallBackSuccess.onSuccess(user)
        }

    }

    override fun getItemCount() = values.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameUser: TextView
        val numberUser: TextView

        init {
            nameUser = view.findViewById(R.id.nameTextView)
            numberUser = view.findViewById(R.id.numberTextView)
        }
    }

}