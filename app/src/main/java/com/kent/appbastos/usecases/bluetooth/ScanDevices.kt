package com.kent.appbastos.usecases.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kent.appbastos.R
import com.kent.appbastos.databinding.ActivityScanDevicesBinding
import com.kent.appbastos.model.adapter.RecyclerViewAdapterDevicesListBT
import com.kent.appbastos.model.util.DeviceBT
import com.kent.appbastos.model.util.EventCallBackDeviceBT
import com.kent.appbastos.model.util.Keys
import java.io.*
import java.util.*

class ScanDevices : AppCompatActivity() {

    private lateinit var bluetoothManager : BluetoothManager
    private var bluetoothAdapter : BluetoothAdapter? = null

    private lateinit var outputStream: OutputStream
    private lateinit var inputStream: InputStream
    private lateinit var pref: SharedPreferences


    private var macAddress: String = "----"

    companion object {
        var myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private var bluetoothSocket: BluetoothSocket? = null
    }

    private val bluetoothPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted:Boolean ->
        if (isGranted) {
            bluetoothManager = getSystemService(BluetoothManager::class.java)
            bluetoothAdapter = bluetoothManager.adapter
            btnPermissions = true
            if (bluetoothAdapter?.isEnabled == false){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            }else{
                findDevices()
            }
        }
    }

    private val btActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result : ActivityResult ->
        if(result.resultCode == RESULT_OK){
           findDevices()
        }
    }

    private var btnPermissions = false
    private val RESULT_OK = 100
    lateinit var binding: ActivityScanDevicesBinding
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanDevicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.result.text = "Por favor voncule un \ndispositivo primero"

        pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        val mac = pref.getString(Keys.MAC, Keys.ERROR)
        if (mac != Keys.ERROR && mac != null){
            Toast.makeText(this@ScanDevices, mac, Toast.LENGTH_LONG).show()
            bluetoothManager = getSystemService(BluetoothManager::class.java)
            bluetoothAdapter = bluetoothManager.adapter
            val successfulConnection = connectDeviceBT(mac)
            if(successfulConnection) {
                printData()
            }else{
                Toast.makeText(this, "Sucedio un error al conectarse", Toast.LENGTH_LONG).show()
                val editor = pref.edit()
                editor.remove(Keys.MAC)
                editor.apply()
            }
            finish()
        }


        //assign toolbar, textView of dateSelect and his father, and config
        toolbar = findViewById(R.id.include)

        //to config toolbar
        toolbar.title = "Dispositivos"
        setSupportActionBar(toolbar)

        // Habilitar la flecha de atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        verificationBT()
    }

    //Create option for the Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_devices_bluetooth, menu)
        val itemUpdateDevices = menu!!.findItem(R.id.update)
        itemUpdateDevices.setOnMenuItemClickListener {
            verificationBT()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun verificationBT():Boolean{
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        if(bluetoothAdapter == null){
            Toast.makeText(this@ScanDevices, "Bluetooth no compatible", Toast.LENGTH_LONG).show()
            onBackPressedDispatcher.onBackPressed()
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                bluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
            }else{
                bluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_ADMIN)
            }
        }
        return true
    }



    @SuppressLint("MissingPermission")
    private fun findDevices(){
        binding.result.visibility = TextView.VISIBLE
        binding.scanDevicesList.visibility = RecyclerView.GONE
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        if(pairedDevices!!.isNotEmpty()){
            val devicesBT: MutableList<DeviceBT> = ArrayList()
            for(device in pairedDevices){
                if(device.bluetoothClass.deviceClass == 1664 ){
                    val deviceBT  = if(macAddress == device.address){ DeviceBT(device.name, device.address, true) }
                    else{ DeviceBT(device.name, device.address, false) }
                    devicesBT.add(deviceBT)
                }
            }
            binding.scanDevicesList.layoutManager = LinearLayoutManager(this@ScanDevices)
            binding.scanDevicesList.adapter = RecyclerViewAdapterDevicesListBT(
                devicesBT,
                object : EventCallBackDeviceBT {
                    override fun onClick(deviceBT: DeviceBT, viewHolder: RecyclerView.ViewHolder) {
                        val successfulConnection = connectDeviceBT(deviceBT.address)

                        if(!successfulConnection) deviceBT.isConnected = false
                        else{
                            Toast.makeText(this@ScanDevices, "Conectado a: ${deviceBT.name}", Toast.LENGTH_LONG).show()
                            deviceBT.isConnected = true
                            macAddress = deviceBT.address
                            // date of dispositive connect
                            binding.dispositiveConnect.root.visibility = LinearLayoutCompat.VISIBLE
                            binding.dispositiveConnect.deviceName.text = deviceBT.name
                            binding.dispositiveConnect.deviceMac.text = deviceBT.address
                            binding.dispositiveConnect.deviceIsConnect.text = "Conectado, click para desconectar"
                            binding.dispositiveConnect.deviceIsConnect.setTextColor(Color.parseColor("#FF0000"))
                            // btn of print activate
                            binding.btnPrint.visibility = Button.VISIBLE
                            //Click to dispositive conect
                            binding.dispositiveConnect.root.setOnClickListener {
                                macAddress = "----"
                                bluetoothSocket?.close()
                                binding.dispositiveConnect.root.visibility = LinearLayoutCompat.GONE
                                binding.btnPrint.visibility = Button.GONE
                                findDevices()
                            }
                            //Click btn print
                            binding.btnPrint.setOnClickListener {
                                printData()
                            }
                        }
                        findDevices()
                    }

                }
            )
            binding.result.visibility = TextView.GONE
            binding.scanDevicesList.visibility = RecyclerView.VISIBLE
        }else{
            binding.result.visibility = TextView.VISIBLE
            binding.scanDevicesList.visibility = RecyclerView.GONE
            binding.result.text = "Dipositivos no encontrados, \nvincule la impresora primero"
            Toast.makeText(this, "Dipositivos no encontrados", Toast.LENGTH_LONG).show()
            return
        }
    }


    private fun connectDeviceBT(mac: String): Boolean{
        try {
            val edit = pref.edit()
            edit.putString(Keys.MAC, mac)
            edit.apply()
            bluetoothSocket?.close()
            Log.i("INFO:", "Iniciando conexion")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            bluetoothAdapter?.cancelDiscovery()
            val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(mac)
            Log.i("INFO:", "$device")
            Log.i("INFO:", "MAC: $mac")
            if(device != null){
                bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                Log.i("INFO:", "$bluetoothSocket")
                bluetoothSocket!!.connect()
                outputStream = bluetoothSocket!!.outputStream
                inputStream = bluetoothSocket!!.inputStream
                return true
            }
            return false
        }catch (e: IOException){
            Log.e("ERROR:", "error ${e.message}")
            e.printStackTrace()
            disconnectBT()
            Toast.makeText(this@ScanDevices, "Verifique si el dispositivo esta prendido", Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun printData( ) {
        try {
            val separate = if (intent.extras?.getString(Keys.NUMBER_CLIENT, Keys.ERROR) != "null") "- - - - - - - - - - - - - - - -\n" +
                    "FECHA: ${intent.extras?.getString(Keys.DATE_TIME, Keys.ERROR)}\n\n" +
                    "REFERENCIA: ${intent.extras?.getString(Keys.REFERENCE, Keys.ERROR)}\n\n"  +
                    "NOMBRE DEL CLIENTE: ${intent.extras?.getString(Keys.NAME_CLIENT, Keys.ERROR)}\n\n" +
                    "NUMERO DEL CLIENTE: ${intent.extras?.getString(Keys.NUMBER_CLIENT, Keys.ERROR)}\n\n" +
                    "TIPO DEL PRODUCTO: ${intent.extras?.getString(Keys.TYPE, Keys.ERROR)}\n\n" +
                    "CATEGORIA DEL PRODUCTO: ${intent.extras?.getString(Keys.CATEGORY, Keys.ERROR)}\n\n" +
                    "VALOR POR UNIDAD: ${intent.extras?.getString(Keys.VALUE_UNIT, Keys.ERROR)}\n\n" +
                    "CANTIDAD: ${intent.extras?.getString(Keys.AMOUNT, Keys.ERROR)}\n\n" +
                    "VALOR TOTAL: ${intent.extras?.getString(Keys.VALUE_TOTAL, Keys.ERROR)}}\n" +
                    "- - - - - - - - - - - - - - - -\n"
            else "- - - - - - - - - - - - - - - -\n\n\n" +
                    "FECHA: ${intent.extras?.getString(Keys.DATE_TIME, Keys.ERROR)}\n\n" +
                    "REFERENCIA: ${intent.extras?.getString(Keys.REFERENCE, Keys.ERROR)}\n\n"  +
                    "NOMBRE DEL CLIENTE: ${intent.extras?.getString(Keys.NAME_CLIENT, Keys.ERROR)}\n\n" +
                    "TIPO DEL PRODUCTO: ${intent.extras?.getString(Keys.TYPE, Keys.ERROR)}\n\n" +
                    "CATEGORIA DEL PRODUCTO: ${intent.extras?.getString(Keys.CATEGORY, Keys.ERROR)}\n\n" +
                    "VALOR POR UNIDAD: ${intent.extras?.getString(Keys.VALUE_UNIT, Keys.ERROR)}\n\n" +
                    "CANTIDAD: ${intent.extras?.getString(Keys.AMOUNT, Keys.ERROR)}\n\n" +
                    "VALOR TOTAL: ${intent.extras?.getString(Keys.VALUE_TOTAL, Keys.ERROR)}}\n" +
                    "- - - - - - - - - - - - - - - -\n\n" +
                    "\n"
            val printWriter = PrintWriter(bluetoothSocket!!.outputStream, true)

            printWriter.flush()
            printWriter.flush()
            bluetoothSocket!!.outputStream.write(ESC_ALIGN_LEFT)
            printWriter.println(separate)
            printWriter.flush()
        } catch (ex: Exception) {
            ex.printStackTrace()
            Toast.makeText(this, "error durante la impresion", Toast.LENGTH_LONG).show()
        }
    }

    val ESC_ALIGN_LEFT = byteArrayOf(0x1b, 'a'.code.toByte(), 0x00)

    // Disconnect Printer //
    fun disconnectBT() {
        try {
            bluetoothSocket?.close()
            outputStream.close()
            inputStream.close()
            //lblPrinterName.setText("Printer Disconnected.");
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}