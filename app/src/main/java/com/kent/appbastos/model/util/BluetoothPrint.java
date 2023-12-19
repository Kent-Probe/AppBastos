package com.kent.appbastos.model.util;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by JOHNHY on 27/02/2018.
 */

public class BluetoothPrint {
    // ArrayList<DetallePedido> arrayList = new ArrayList<>();
    Context context;
    // String cliente,fecha,total;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    Resources resources;


    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    public static final byte[] ESC_ALIGN_LEFT = new byte[]{0x1b, 'a', 0x00};
    public static final byte[] ESC_ALIGN_RIGHT = new byte[]{0x1b, 'a', 0x02};
    public static final byte[] ESC_ALIGN_CENTER = new byte[]{0x1b, 'a', 0x01};
    public static final byte[] ESC_CANCEL_BOLD = new byte[]{0x1B, 0x45, 0};


    public static byte[] format = {27, 33, 0};
    public static byte[] arrayOfByte1 = {27, 33, 0};


    public BluetoothPrint(Context context, Resources resources) {
        this.context = context;
        this.resources = resources;
    }

    public void FindBluetoothDevice() {

        try {
            Toast.makeText(context, "AQUI YA ESTA", Toast.LENGTH_LONG).show();
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                //  lblPrinterName.setText("No Bluetooth Adapter found");
            }
            if (bluetoothAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(enableBT);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if (pairedDevice.size() > 0) {
                for (BluetoothDevice pairedDev : pairedDevice) {
                    //Toast.makeText(context,pairedDev.getName().toString(),Toast.LENGTH_LONG).show();
                    // My Bluetoth printer name is BTP_F09F1A
                    Log.d("DEVICE: ", pairedDev.getName());
                    if (pairedDev.getName().equals("BlueTooth Printer")) {
                        bluetoothDevice = pairedDev;
                        //Toast.makeText(context,"Bluetooth Conectado Correctamente",Toast.LENGTH_LONG).show();
                        //lblPrinterName.setText("Bluetooth Printer Attached: "+pairedDev.getName());
                        break;
                    }
                }
                if (bluetoothDevice == null) {
                    Toast.makeText(context, "No fue posible conectar con la impresora intente de nuevo", Toast.LENGTH_LONG).show();
                }
            }

            //lblPrinterName.setText("Bluetooth Printer Attached");
        } catch (Exception ex) {
            Log.e("ERROR:", ex.getMessage());
            ex.printStackTrace();
        }

    }

    // Open Bluetooth Printer

    public void openBluetoothPrinter() {
        try {

            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenData();

        }catch (Exception ex){
            Log.e("ERROR:", ex.getMessage());
        }
    }

    public boolean checkConnection(){
        if(bluetoothSocket!=null){
            if(bluetoothSocket.isConnected()){
                Toast.makeText(context,"Estoy aqui esta conectado",Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }

    public void beginListenData(){
        try{

            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte, StandardCharsets.US_ASCII);
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //lblPrinterName.setText(data);
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }

                }
            });

            thread.start();
        }catch (Exception ex){
            Log.e("ERROR:", ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Printing Text to Bluetooth Printer //
    public void printData() {
        try{
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

            String msg = "-----------test";

            outputStream.write(ESC_ALIGN_CENTER);
            outputStream.write(msg.getBytes());
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
            Log.e("ERROR:", ex.getMessage());
        }
    }

    // Disconnect Printer //
    public void disconnectBT() {
        try {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            //lblPrinterName.setText("Printer Disconnected.");
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e("ERROR:", ex.getMessage());
        }
    }
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(resources,
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(ESC_ALIGN_CENTER);
                outputStream.write(command);

            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
            Toast.makeText(context,"Ahora estoy aca en imagen"+e,Toast.LENGTH_LONG).show();
            Log.e("ERROR:", e.getMessage());
        }
    }

}
