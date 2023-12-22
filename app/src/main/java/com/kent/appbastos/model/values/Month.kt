package com.kent.appbastos.model.values

import java.util.*

class Month(
    private val id: Int = 0,
    val month: String = ""
) {

    val months: ArrayList<Month> = ArrayList()

    fun initMonth(){
        val january = Month(1, "Enero")
        months.add(january)
        val february = Month(2, "Febrero")
        months.add(february)
        val march = Month(3, "Marzo")
        months.add(march)
        val april = Month(4, "Abril")
        months.add(april)
        val may = Month(5, "Mayo")
        months.add(may)
        val june = Month(6, "Junio")
        months.add(june)
        val july = Month(7, "Julio")
        months.add(july)
        val august = Month(8, "Agosto")
        months.add(august)
        val september = Month(9, "Septiembre")
        months.add(september)
        val october = Month(10, "Octubre")
        months.add(october)
        val november = Month(11, "Nobiembre")
        months.add(november)
        val december = Month(12, "Diciembre")
        months.add(december)
    }

    fun monthName():Vector<String>{
        val names = Vector<String>()
        months.forEach {
            names.add(it.month)
        }
        return names
    }

}