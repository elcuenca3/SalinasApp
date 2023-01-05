package com.example.sali

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (existeArchivo((fileList()), "usuarios.txt")){
            var contenido = ""
            var archivo = InputStreamReader(openFileInput("usuarios.txt"))
            var bf = BufferedReader(archivo)
            var linea = bf.readLine()
            while (linea != null){
                contenido = contenido + linea + "\n"
                linea = bf.readLine()
            }
            celularUser?.setText(contenido)

        }
        registro.setOnClickListener{
            var saltar: Intent = Intent(this, Envio::class.java)
            startActivity(saltar)
        }
    }
    fun guardarArchivo(view: View){
        var archivo = OutputStreamWriter(openFileOutput("usuarios.txt", Activity.MODE_PRIVATE))
        archivo.write(celularUser.text.toString())
        archivo.flush()
        archivo.close()
    }
    fun existeArchivo(archivos: Array<String>, archivo:String): Boolean{
        archivos.forEach {
            if (archivo == it){
                return true
            }
        }
        return false
    }
}