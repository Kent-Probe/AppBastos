package com.kent.appbastos.model.values

import android.content.Context
import com.kent.appbastos.usecases.remarks.AddRemarks
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.LocalDateTime

class CreditSaleClass(
    var marca:String,
    var nameClient:String,
    var numberClient:String,
    var nameProduct:String,
    var valueUnit:Float,
    var valueAmount:Float,
    var valueTotal:Float,
    var dateTime:LocalDateTime,
    var consecutive:String,
    var type:String
) {
    constructor() : this("", "","","",0f,0f,0f, LocalDateTime.now(),"", "")
    //var currentBalance:Float = 0f

    //Methods of Archive
    fun saveArchive(context: Context):Boolean{
        var successful = true
        try{
            val archive =
                OutputStreamWriter(context.openFileOutput("CreditSale.txt", Context.MODE_PRIVATE))
            archive.write("Nombre Client: $nameClient" +
                    "\nNombre del producto: $nameProduct"+
                    "\nNumero del cliente: $numberClient" +
                    "\nMarca: $marca" +
                    "\nvalor por unidad: $valueUnit" +
                    "\nCantidad: $valueAmount" +
                    "\nValor total: $valueTotal" +
                    "\nFecha: $dateTime" +
                    "\nconsecutivo: $consecutive" +
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
                InputStreamReader(context.openFileInput("CreditSale.txt"))
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
                "\nNumero del cliente: $numberClient" +
                "\nMarca: $marca" +
                "\nvalor por unidad: $valueUnit" +
                "\nCantidad: $valueAmount" +
                "\nValor total: $valueTotal" +
                "\nFecha: $dateTime" +
                "\nconsecutivo: $consecutive"+
                "\nType: $type")
    }

}