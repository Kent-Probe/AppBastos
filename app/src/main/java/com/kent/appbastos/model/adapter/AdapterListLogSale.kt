package com.kent.appbastos.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.databinding.LogContentItemBinding
import com.kent.appbastos.model.values.LogSale

class AdapterListLogSale(private val logSale: List<LogSale>, private val interactionClickItem: Interaction): RecyclerView.Adapter<AdapterListLogSale.LogHolder>() {

    lateinit var interaction: Interaction

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LogHolder(layoutInflater.inflate(R.layout.log_content_item, parent, false))
    }

    override fun getItemCount(): Int = logSale.size

    override fun onBindViewHolder(holder: LogHolder, position: Int) {
        interaction = interactionClickItem
        holder.render(logSale[position], interaction)

        /*holder.view.setOnClickListener {
            interactionClickItem.onClickItem()
        }*/
    }

    class LogHolder(val view: View):RecyclerView.ViewHolder(view){
        private val binding: LogContentItemBinding = LogContentItemBinding.bind(view)

        val ref = "Referencia: %s"
        val total = "Total: %s"

        fun render(logSale: LogSale, int: Interaction){
            binding.invoiceRef.text = ref.format(logSale.ref)
            binding.total.text = total.format(logSale.total)
            view.setOnClickListener {
                int.onClickItem()
            }
        }
    }

    interface Interaction{
        fun onClickItem()
    }
}