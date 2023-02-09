package com.kent.appbastos.model.values

import android.content.Context
import com.kent.appbastos.usecases.remarks.AddRemarks
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.LocalDateTime

class CashSaleClass(
    var nameClient: String,
    var nameProduct: String,
    var marca: String,
    private var amount: Float,
    var valueUnit: Float,
    var valueTotal: Float,
    var dateTime: LocalDateTime,
    var consecut: String,
    var type: String
) {
    constructor() : this("","","",0f,0f,0f, LocalDateTime.now(),"", "")


    //Methods of Archive
    fun saveArchive(context: Context):Boolean{
        var successful = true
        try{
            val archive =
                OutputStreamWriter(context.openFileOutput("CashSale.txt", Context.MODE_PRIVATE))
            archive.write("Nombre Client: $nameClient" +
                    "\nNombre del producto: $nameProduct"+
                    "\nMarca: $marca" +
                    "\nvalor por unidad: $valueUnit" +
                    "\nCantidad: $amount" +
                    "\nValor total: $valueTotal" +
                    "\nFecha: $dateTime" +
                    "\nconsecutivo: $consecut"+
                    "\nType: $type"
            )
            archive.flush()
            archive.close()
        }catch (e: IOException){
            successful = false
        }
        return successful
    }

    fun openArchive(context: AddRemarks):String{
        var successful: String
        try {
            val archive =
                InputStreamReader(context.openFileInput("CashSale.txt"))
            val buffer = BufferedReader(archive)
            var text = buffer.readLine()
            var textAll = ""
            while (text != null){
                textAll += text + "\n"
                text = buffer.readLine()
            }
            buffer.close()
            archive.close()
            successful = textAll
        }catch (e: IOException){
            successful = "nada"
        }
        return successful
    }


    fun dateOfClass(): String{
        return ("Nombre Client: $nameClient" +
                "\nNombre del producto: $nameProduct"+
                "\nMarca: $marca" +
                "\nvalor por unidad: $valueUnit" +
                "\nCantidad: $amount" +
                "\nValor total: $valueTotal" +
                "\nFecha: $dateTime" +
                "\nconsecutivo: $consecut"+
                "\nType: $type")
    }
}