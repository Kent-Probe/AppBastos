package com.kent.appbastos.usecases.share

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.kent.appbastos.R
import com.kent.appbastos.model.util.Keys
import java.io.File
import java.io.FileOutputStream
import java.util.*

class Share : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)


        val data :MutableMap<String, String?> = hashMapOf(
            Keys.NAME_CLIENT to intent.extras?.getString(Keys.NAME_CLIENT, Keys.ERROR),
            Keys.DATE_TIME to intent.extras?.getString(Keys.DATE_TIME, Keys.ERROR),
            Keys.TYPE to intent.extras?.getString(Keys.TYPE, Keys.ERROR),
            Keys.CATEGORY to intent.extras?.getString(Keys.CATEGORY, Keys.ERROR),
            Keys.VALUE_TOTAL to intent.extras?.getString(Keys.VALUE_TOTAL, Keys.ERROR),
            Keys.AMOUNT to intent.extras?.getString(Keys.AMOUNT, Keys.ERROR),
            Keys.VALUE_UNIT to intent.extras?.getString(Keys.VALUE_UNIT, Keys.ERROR),
            Keys.NUMBER_CLIENT to intent.extras?.getString(Keys.NUMBER_CLIENT, Keys.ERROR),
            Keys.REFERENCE to intent.extras?.getString(Keys.REFERENCE, Keys.ERROR)
        )

        createImage(data)

        //Change value of name Profile
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString(Keys.PROFILE, null).toString()
        val txtUserName: TextView = findViewById(R.id.txtUserName)
        txtUserName.text = profile

        //Variables Buttons
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnBackHome: Button = findViewById(R.id.btnBackHome)
        val btnShare: Button = findViewById(R.id.btnShare)
        val btnPrintOut: Button = findViewById(R.id.btnPrintOut)

        btnBack.setOnClickListener {
            finish()
        }
        btnBackHome.setOnClickListener {
            finish()
        }

        btnShare.setOnClickListener {
            share(data)
            Toast.makeText(this, "Exito", Toast.LENGTH_LONG).show()
        }

        //Construction
        btnPrintOut.setOnClickListener {
            
        }
    }

    private fun share(data: MutableMap<String, String?>){
        try{
            val imageFile = File(getExternalFilesDir(null), "/receipt/${data[Keys.REFERENCE]}.jpg")
            val uri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", imageFile)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                setPackage("com.whatsapp")
            }
            startActivity(Intent.createChooser(shareIntent, "Compartir imagen"))
        }catch (e :Exception){
            Toast.makeText(this, "no se encontro WhatsApp instalado", Toast.LENGTH_LONG).show()
        }


    }

    private fun createImage(data: MutableMap<String, String?>) {
        //Exceptions
        try {
            //Options of bitmap
            val options = BitmapFactory.Options()
            options.inMutable = true

            //create bitmap and add the template, and create canvas with bitmap
            val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.plantilla, options)
            val canvas = Canvas(bitmap)

            //create brunch for paint
            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 40f
                textAlign = Paint.Align.LEFT
            }

            val formats = Vector(
                listOf(
                    Keys.FORMAT_RECEIPT_DATE_TIME,
                    Keys.FORMAT_RECEIPT_CLIENT,
                    Keys.FORMAT_RECEIPT_NUMBER,
                    Keys.FORMAT_RECEIPT_TYPE,
                    Keys.FORMAT_RECEIPT_CATEGORY,
                    Keys.FORMAT_RECEIPT_VALUE_UNIT,
                    Keys.FORMAT_RECEIPT_AMOUNT,
                    Keys.FORMAT_RECEIPT_VALUE_TOTAL,
                    Keys.FORMAT_RECEIPT_REFERENCE
                )
            )

            val dataValue = Vector(
                listOf(
                    data[Keys.DATE_TIME].toString(),
                    data[Keys.NAME_CLIENT].toString(),
                    Keys.formatNumber(data[Keys.NUMBER_CLIENT].toString()),
                    data[Keys.TYPE].toString(),
                    data[Keys.CATEGORY].toString(),
                    data[Keys.VALUE_UNIT].toString(),
                    data[Keys.AMOUNT].toString(),
                    data[Keys.VALUE_TOTAL].toString(),
                    data[Keys.REFERENCE].toString()
                )
            )

            //val for config position and axis x and y
            val x = 50f
            val y = 370f
            var h = 0
            for((n, format) in formats.withIndex()){
                if(!dataValue[n].equals("null")){
                    canvas.drawText(format.format(dataValue[n]), x, y + h, paint)
                    h += 100
                }
            }

            //create the folder
            val folder = File(applicationContext.getExternalFilesDir(null), "receipt")
            if (!folder.exists()) {
                folder.mkdirs()
            }

            //Create image
            val file = File(folder, "${data[Keys.REFERENCE]}.jpg")
            file.createNewFile()

            //open image, compress bitmap after draw,and add at file, close the stream
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

        } catch (e: Exception) {
            setContentView(R.layout.activity_share)
            findViewById<Button>(R.id.btnBackHome).visibility = Button.GONE
            findViewById<Button>(R.id.btnPrintOut).visibility = Button.GONE
            findViewById<Button>(R.id.btnShare).visibility = Button.GONE
            Toast.makeText(this, "Error, contacta al admin", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}

