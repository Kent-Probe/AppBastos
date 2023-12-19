package com.kent.appbastos.usecases.share


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.kent.appbastos.R
import com.kent.appbastos.ScanDevices
import com.kent.appbastos.databinding.ActivityShareBinding
import com.kent.appbastos.model.util.Keys
import java.io.File
import java.io.FileOutputStream
import java.util.*


class Share : AppCompatActivity() {

    private val bluetoothPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted:Boolean ->
        if (isGranted) {
            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            btnPermissions = true
            if (bluetoothAdapter?.isEnabled == false){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            }else{
                openScanDevices()
            }
        }
    }

    private val btActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result : ActivityResult ->
        if(result.resultCode == RESULT_OK){
            openScanDevices()
        }else if(result.resultCode == RESULT_CANCELED){
            Toast.makeText(this@Share, "Bluetooth no activado", Toast.LENGTH_LONG).show()
        }
    }

    private fun openScanDevices() {
        val intent = Intent(this, ScanDevices::class.java).apply {
            putExtra(Keys.DATE_TIME, intent.extras?.getString(Keys.DATE_TIME, Keys.ERROR))
            putExtra(Keys.NAME_CLIENT, intent.extras?.getString(Keys.NAME_CLIENT, Keys.ERROR))
            putExtra(Keys.TYPE, intent.extras?.getString(Keys.TYPE, Keys.ERROR))
            putExtra(Keys.CATEGORY, intent.extras?.getString(Keys.CATEGORY, Keys.ERROR))
            putExtra(Keys.VALUE_TOTAL, intent.extras?.getString(Keys.VALUE_TOTAL, Keys.ERROR))
            putExtra(Keys.AMOUNT, intent.extras?.getString(Keys.AMOUNT, Keys.ERROR))
            putExtra(Keys.VALUE_UNIT, intent.extras?.getString(Keys.VALUE_UNIT, Keys.ERROR))
            putExtra(Keys.REFERENCE, intent.extras?.getString(Keys.REFERENCE, Keys.ERROR))
            putExtra(Keys.NUMBER_CLIENT, intent.extras?.getString(Keys.NUMBER_CLIENT, Keys.ERROR))
        }
        startActivity(intent)
    }

    private var btnPermissions: Boolean = false
    private lateinit var biding: ActivityShareBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(biding.root)


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
            try{
                val bluetoothManager = getSystemService(BluetoothManager::class.java)
                val bluetoothAdapter = bluetoothManager.adapter
                if(bluetoothAdapter == null){
                    Toast.makeText(this@Share, "Bluetooth no compatible", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    bluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
                }else{
                    bluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_ADMIN)
                }
                //val intent = Intent(this, ScanDevices::class.java)
                //startActivity(intent)
            }catch (e: Exception){
                Log.e("ERROR", e.message.toString())
            }

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

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}

