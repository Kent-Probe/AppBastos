package com.kent.appbastos.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.model.util.DeviceBT
import com.kent.appbastos.model.util.EventCallBackDeviceBT

class RecyclerViewAdapterDevicesListBT(
    private val values: List<DeviceBT>,
    private val eventCallback: EventCallBackDeviceBT
): RecyclerView.Adapter<RecyclerViewAdapterDevicesListBT.VieHolder>() {

    private val valuesFilter = values.filter { !it.isConnected }

    class VieHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView
        val address: TextView
        val deviceIsConnect: TextView

        init {
            name = view.findViewById(R.id.deviceName)
            address = view.findViewById(R.id.deviceMac)
            deviceIsConnect = view.findViewById(R.id.deviceIsConnect)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device_list, parent, false)
        return VieHolder(view)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        val device = valuesFilter[position]

        holder.name.text = device.name
        holder.address.text = device.address
        holder.deviceIsConnect.text = if (device.isConnected) "Conectado" else "no conectado, click para conectar"



        holder.itemView.setOnClickListener {
            if(device.isConnected){
                return@setOnClickListener
            }
            valuesFilter.forEach { it.isConnected = false }
            device.isConnected = true
            eventCallback.onClick(device, holder)
        }
    }

    override fun getItemCount() = valuesFilter.size




}