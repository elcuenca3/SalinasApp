package com.example.sali

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registro.setOnClickListener{
            var saltar: Intent = Intent(this, Envio::class.java)
            startActivity(saltar)

        }
    }
}