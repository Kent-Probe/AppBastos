package com.kent.appbastos.services

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*

class BluetoothService(private val context: Context, private val listener: BluetoothListener) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private var bluetoothSocket: BluetoothSocket? = null

    interface BluetoothListener {
        fun onConnectionSuccess()
        fun onConnectionFailure(error: String)
        fun onDisconnect()
    }

    fun connect(deviceAddress: String) {
        if (bluetoothAdapter == null) {
            listener.onConnectionFailure("Bluetooth not supported on this device.")
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            listener.onConnectionFailure("Bluetooth is not enabled.")
            return
        }

        val device: BluetoothDevice = bluetoothAdapter!!.getRemoteDevice(deviceAddress)

        try {
            // Crear un socket Bluetooth
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))

            // Conectar el socket
            bluetoothSocket!!.connect()

            listener.onConnectionSuccess()

        } catch (e: IOException) {
            listener.onConnectionFailure("Connection failed: ${e.message}")
            Log.e("BluetoothService", "Error during connection", e)
        }
    }

    fun disconnect() {
        try {
            bluetoothSocket?.close()
            listener.onDisconnect()
        } catch (e: IOException) {
            Log.e("BluetoothService", "Error during disconnection", e)
        }
    }
}
