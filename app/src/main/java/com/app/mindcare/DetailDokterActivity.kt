package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DetailDokterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dokter)

        // 1. Ambil data yang dikirim dari Adapter tadi
        val nama = intent.getStringExtra("NAMA_DOKTER")
        val harga = intent.getStringExtra("HARGA_DOKTER")
        val gambar = intent.getIntExtra("GAMBAR_DOKTER", 0)

        // 2. Tampilkan ke UI
        findViewById<TextView>(R.id.tvDetailName).text = nama
        findViewById<TextView>(R.id.tvDetailPrice).text = harga
        findViewById<ImageView>(R.id.imgDetailDoctor).setImageResource(gambar)

        // 3. Tombol Lanjut ke Pembayaran
        findViewById<Button>(R.id.btnLanjutBayar).setOnClickListener {

            startActivity(intent)
        }
    }
}