package com.example.sali

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_envio.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class Envio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envio)
        var mensajeFinal = "**messa**content="



        envio.setOnClickListener{
            var mensaje = mensaje.text.toString()
            mensaje = mensaje.replace(" ","%")
            var origen = numOrigen.text.toString()
            var destino = numDestino.text.toString()
            mensajeFinal = mensajeFinal + mensaje + "&phoneOrigin=" + origen + "&phoneDestiny=" + destino
        }
    }
    fun sendFile(context: Context, filePath: String) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            context.startActivity(enableBtIntent)
        }
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        if (pairedDevices?.isEmpty() == true) {
            // There are no paired devices.
            return
        }
        // Loop through paired devices and find the one we want to send the file to.
        for (device in pairedDevices) {
            if (device.name == "My Bluetooth Device") {
                val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                try {
                    socket.connect()
                    val outputStream: OutputStream = socket.outputStream
                    // Send the file over the output stream.
                    val fileInputStream = FileInputStream(filePath)
                    val buffer = ByteArray(1024)
                    var bytesRead: Int = fileInputStream.read(buffer)
                    while (bytesRead > 0) {
                        outputStream.write(buffer, 0, bytesRead)
                        bytesRead = fileInputStream.read(buffer)
                    }
                    fileInputStream.close()
                    outputStream.close()
                    socket.close()
                } catch (e: IOException) {
                    Log.e("Bluetooth", "Error occurred when sending file.", e)
                }
                break
            }
        }
    }
}