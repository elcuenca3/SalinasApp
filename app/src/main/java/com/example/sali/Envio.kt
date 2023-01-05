package com.example.sali

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_envio.*
import java.io.IOException
import java.io.OutputStream
import java.util.*

const val REQUEST_ENABLE_BT = 1

class Envio : AppCompatActivity() {

    //BluetoothAdapter
    lateinit var mBtAdapter: BluetoothAdapter
    var mAddressDevices: ArrayAdapter<String>? = null
    var mNameDevices: ArrayAdapter<String>? = null

    companion object {
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private var m_bluetoothSocket: BluetoothSocket? = null

        var m_isConnected: Boolean = false
        lateinit var m_address: String
    }

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
            sendStringAsFile("LAPTOP-7UA4PLAP", mensajeFinal )
        }
    }
    fun sendStringAsFile(deviceName: String, string: String) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            // Bluetooth is not enabled.
            return
        }
        try {
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices

        if (pairedDevices?.isEmpty() == true) {
            // There are no paired devices.
            return
        }
        // Loop through paired devices and find the one we want to send the file to.
        if (pairedDevices != null) {
            for (device in pairedDevices) {
                if (ActivityCompat.checkSelfPermission(
                        this,
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
                if (device.name == deviceName) {
                    val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                    val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                    try {
                        socket.connect()
                        val outputStream: OutputStream = socket.outputStream
                        // Send the file over the output stream.
                        outputStream.write("file.txt".toByteArray())
                        outputStream.write(string.toByteArray())
                        outputStream.close()
                        socket.close()
                    } catch (e: IOException) {
                        Log.e("Bluetooth", "Error occurred when sending file.", e)
                    }
                    break
                }
            }
        }        }catch (e:java.lang.Error){
            print("Error " + e)
        }
    }
}