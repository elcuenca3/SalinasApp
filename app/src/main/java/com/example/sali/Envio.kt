import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.util.*
import android.Manifest
import com.example.sali.R

const val REQUEST_BLUETOOTH_PERMISSION = 1
const val REQUEST_ENABLE_BT = 2

class Envio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envio)

        // Check if Bluetooth is supported on the device
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if Bluetooth is not enabled, and ask the user to enable it
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        // Check if the app has the BLUETOOTH permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            // Request the BLUETOOTH permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH), REQUEST_BLUETOOTH_PERMISSION)
        } else {
            // Permission has already been granted
            sendFileOverBluetooth()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_BLUETOOTH_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted
                    sendFileOverBluetooth()
                } else {
                    // Permission has been denied
                }
                return
            }
            else -> {
                // Handle other permission requests
            }
        }
    }

    private fun sendFileOverBluetooth() {
        val fileToSend = File("/path/to/file.txt")
        val fileInputStream = FileInputStream(fileToSend)
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        if (pairedDevices?.isNotEmpty() == true) {
            // There are paired devices. Get the name and address of each paired device.
            for (device in pairedDevices) {
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address
                if (deviceName == "My Bluetooth Device") {
                    // This is the device we want to connect to
                    val socket: BluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(uuid)
                    socket.connect()
                    val outputStream: OutputStream = socket.outputStream
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (fileInputStream.read(buffer).also { bytesRead = it } > 0) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                    fileInputStream.close()
                    outputStream.close()
                    socket.close()
                }
            }
        }
    }
}