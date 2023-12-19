package com.kent.appbastos.services.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * Created by JOHNHY on 27/02/2018.
 */
class BluetoothPrint(// Agregar datos a imprimir <detalles del pedido>
    var context: Context, var resources: Resources, var bluetoothManager: BluetoothManager,
    var bluetoothAdapter: BluetoothAdapter?, var bluetoothSocket: BluetoothSocket? = null,
    var bluetoothDevice: BluetoothDevice? = null, var myUUID: UUID
) {
    // String cliente,fecha,total;
    var outputStream: OutputStream? = null
    var inputStream: InputStream? = null
    var thread: Thread? = null
    lateinit var readBuffer: ByteArray
    var readBufferPosition = 0
    var stopWorker = false

    // Open Bluetooth Printer
    //TODO("Luego implementarlo")
    fun openBluetoothPrinter() {
        try {
            //Standard uuid from string //
            val uuidSting = myUUID
            bluetoothSocket = bluetoothDevice?.createRfcommSocketToServiceRecord(uuidSting)
            bluetoothSocket!!.connect()
            outputStream = bluetoothSocket!!.outputStream
            inputStream = bluetoothSocket!!.inputStream
            beginListenData()
        } catch (ex: Exception) {
            Toast.makeText(context, "error, revise si esta prendida la impresora", Toast.LENGTH_LONG).show()
        }
    }

    fun checkConnection(): Boolean {
        if (bluetoothSocket != null) {
            if (bluetoothSocket!!.isConnected) {
                Toast.makeText(context, "Estoy aqui esta conectado", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return false
    }

    fun beginListenData() {
        try {
            // Manejador para actualizar la interfaz de usuario desde el hilo de fondo
            val handler = Handler(Looper.getMainLooper())

            // Delimitador que se utiliza para separar los paquetes de datos
            val delimiter: Byte = 10

            // Bandera que controla el bucle del hilo
            stopWorker = false

            // Posición actual en el buffer de lectura
            var readBufferPosition = 0

            // Buffer para almacenar los datos leídos
            val readBuffer = ByteArray(1024)

            // Inicialización y arranque del hilo de fondo
            val thread = Thread {
                while (!Thread.currentThread().isInterrupted && !stopWorker) {
                    try {
                        // Verificar si hay datos disponibles para leer
                        val byteAvailable = inputStream!!.available()

                        if (byteAvailable > 0) {
                            // Crear un array para almacenar los bytes disponibles
                            val packetByte = ByteArray(byteAvailable)

                            // Leer los bytes disponibles
                            inputStream?.read(packetByte)

                            // Iterar a través de los bytes leídos
                            for (i in 0 until byteAvailable) {
                                val b = packetByte[i]

                                // Verificar si el byte actual es el delimitador
                                if (b == delimiter) {
                                    // Si es un delimitador, copiar los datos hasta el delimitador en un nuevo array
                                    val encodedByte = ByteArray(readBufferPosition)
                                    System.arraycopy(
                                        readBuffer, 0,
                                        encodedByte, 0,
                                        encodedByte.size
                                    )

                                    // Convertir el array de bytes a una cadena utilizando la codificación US-ASCII
                                    val data = String(encodedByte, Charsets.US_ASCII)

                                    // Reiniciar la posición en el buffer de lectura
                                    readBufferPosition = 0

                                    // Actualizar la interfaz de usuario con los datos recibidos
                                    handler.post {
                                        // lblPrinterName.text = data
                                    }
                                } else {
                                    // Si no es un delimitador, agregar el byte al buffer de lectura
                                    readBuffer[readBufferPosition++] = b
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        // Si ocurre una excepción, detener el hilo
                        stopWorker = true
                        ex.printStackTrace()
                    }
                }
            }

            // Iniciar el hilo de fondo
            thread.start()
        } catch (ex: Exception) {
            // Manejar cualquier excepción que ocurra al iniciar el hilo
            ex.printStackTrace()
        }
    }

    // Printing Text to Bluetooth Printer //
    fun printData(
        //arrayList: ArrayList<DetallePedido?>?,
        fecha: String?,
        cliente: String?,
        total: String?
    ) {
        try {
            // Bold

            /*
            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String hello = "HELLO WORLD \n";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(hello.getBytes());




            // Width
            format[2] = ((byte) (0x20 | arrayOfByte1[2]));
            outputStream.write(ESC_ALIGN_LEFT);
            outputStream.write(hello.getBytes());
            // Underline
            format[2] = ((byte)(0x80 | arrayOfByte1[2]));
            outputStream.write(ESC_ALIGN_RIGHT);
            outputStream.write(hello.getBytes());
            // Small
            format[2] = ((byte)(0x1 | arrayOfByte1[2]));
            outputStream.write(format);
            outputStream.write(hello.getBytes());*/

            //printPhoto(R.drawable.logo);
            val msg = ""
            outputStream!!.write(ESC_ALIGN_CENTER)
            outputStream!!.write(msg.toByteArray())
        } catch (ex: Exception) {
            ex.printStackTrace()
            Toast.makeText(context, "" + ex, Toast.LENGTH_LONG).show()
        }
    }

    // Disconnect Printer //
    fun disconnectBT() {
        try {
            stopWorker = true
            outputStream!!.close()
            inputStream!!.close()
            bluetoothSocket?.close()
            //lblPrinterName.setText("Printer Disconnected.");
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun printPhoto(img: Int) {
        try {
            val bmp: Bitmap = BitmapFactory.decodeResource(
                resources,
                img
            )
            val command = Utils.decodeBitmap(bmp)
            outputStream!!.write(ESC_ALIGN_CENTER)
            outputStream!!.write(command)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PrintTools", "the file isn't exists")
            Toast.makeText(context, "Ahora estoy aca en imagen$e", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        val ESC_ALIGN_LEFT = byteArrayOf(0x1b, 'a'.code.toByte(), 0x00)
        val ESC_ALIGN_RIGHT = byteArrayOf(0x1b, 'a'.code.toByte(), 0x02)
        val ESC_ALIGN_CENTER = byteArrayOf(0x1b, 'a'.code.toByte(), 0x01)
        val ESC_CANCEL_BOLD = byteArrayOf(0x1B, 0x45, 0)
        var format = byteArrayOf(27, 33, 0)
        var arrayOfByte1 = byteArrayOf(27, 33, 0)
    }
}