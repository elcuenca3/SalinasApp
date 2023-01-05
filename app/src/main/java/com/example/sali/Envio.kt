package com.example.sali

import android.bluetooth.BluetoothAdapter
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
            enviarBlue()
        }
    }
    fun enviarBlue(){
        
    }
}