package com.kent.appbastos.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.model.firebase.UserApp
import com.kent.appbastos.model.util.EventCallBackUserApp
import com.kent.appbastos.model.util.Keys

class RecyclerViewAdapterListUsersApp(private val values:List<UserApp>, private val eventCallBackSuccess: EventCallBackUserApp):
    RecyclerView.Adapter<RecyclerViewAdapterListUsersApp.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_app_content, parent, false)

        return ViewHolder(view)
    }

    private fun concatenate(text: String, text2: String):String {
        return text + text2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = values[position]
        holder.nameUser.text = "%s %s".format(user.firstName, user.lastName)
        holder.email.text = concatenate("Correo: ", user.email)
        holder.rol.text = concatenate("Rol: ", user.rol)

        if(user.rol == Keys.ROL_ADMIN){
            holder.btnChangeRol.visibility = Button.GONE
            holder.btnChangeRol.isEnabled = false
        }

        holder.btnChangeRol.setOnClickListener{
            eventCallBackSuccess.onSuccess(user, holder)
        }
    }

    override fun getItemCount() = values.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameUser: TextView
        val email: TextView
        val rol: TextView
        val btnChangeRol: Button

        init {
            nameUser = view.findViewById(R.id.nameTextView)
            email = view.findViewById(R.id.emailTextView)
            rol = view.findViewById(R.id.rolTextView)
            btnChangeRol = view.findViewById(R.id.btnTransformRol)
        }
    }


}