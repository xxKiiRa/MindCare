package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class tampilanAwal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tampilan_awal)

        findViewById<MaterialButton>(R.id.btnMulai).setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}