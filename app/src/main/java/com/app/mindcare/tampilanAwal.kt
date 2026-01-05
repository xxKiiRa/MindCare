package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton


class tampilanAwal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengaktifkan tampilan edge-to-edge untuk pengalaman visual yang lebih modern
        enableEdgeToEdge()
        setContentView(R.layout.activity_tampilan_awal)

        // Tombol "Mulai" untuk mengarahkan pengguna ke halaman Login
        findViewById<MaterialButton>(R.id.btnMulai).setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            // Menutup activity ini agar user tidak bisa kembali ke layar welcome setelah masuk ke login
            finish()
        }
    }
}