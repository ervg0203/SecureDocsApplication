package com.example.securedocs

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var etPin: EditText
    private lateinit var btnAccess: Button
    private lateinit var tvMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPin = findViewById(R.id.etPin)
        btnAccess = findViewById(R.id.btnAccess)
        tvMessage = findViewById(R.id.tvMessage)

        btnAccess.setOnClickListener {
            val pin = etPin.text.toString()
            if (pin.length < 4) {
                tvMessage.text = "PIN must be at least 4 digits."
                return@setOnClickListener
            }

            val intent = Intent(this, FileActivity::class.java)
            intent.putExtra("PIN", pin)
            startActivity(intent)
        }
    }
}
