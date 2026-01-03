package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailDokterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dokter)

        // 1. Inisialisasi View
        val imgDetail = findViewById<ImageView>(R.id.imgDetail)
        val tvName = findViewById<TextView>(R.id.tvNameDetail)
        val tvSpec = findViewById<TextView>(R.id.tvSpecDetail)
        val tvExp = findViewById<TextView>(R.id.tvPengalaman)
        val tvEdu = findViewById<TextView>(R.id.tvAlumnus)
        val tvLoc = findViewById<TextView>(R.id.tvPraktik)
        val tvStrNum = findViewById<TextView>(R.id.tvStr)
        val btnPilih = findViewById<Button>(R.id.btnPilihDetail) // ID sudah diganti

        // 2. Tangkap Data dari Intent
        val nama = intent.getStringExtra("NAMA") ?: "-"
        val spec = intent.getStringExtra("SPESIALIS") ?: "-"
        val harga = intent.getStringExtra("HARGA") ?: "Rp 0"
        val gambar = intent.getIntExtra("GAMBAR", R.drawable.docter_nimas)
        val isOnline = intent.getBooleanExtra("ONLINE", false)
        val pengalaman = intent.getStringExtra("PENGALAMAN") ?: "-"
        val alumnus = intent.getStringExtra("ALUMNUS") ?: "-"
        val praktik = intent.getStringExtra("PRAKTIK") ?: "-"
        val str = intent.getStringExtra("STR") ?: "-"

        // 3. Tampilkan Data
        tvName.text = nama
        tvSpec.text = spec
        tvExp.text = "Pengalaman: $pengalaman"
        tvEdu.text = "Alumnus: $alumnus"
        tvLoc.text = "Praktik di: $praktik"
        tvStrNum.text = "No. STR: $str"
        imgDetail.setImageResource(gambar)

        // 4. Logika Tombol (Warna & Teks sesuai status Online)
        if (!isOnline) {
            btnPilih.alpha = 0.5f
            btnPilih.text = "Dokter Sedang Offline"
            // Opsi: btnPilih.setBackgroundColor(resources.getColor(R.color.mc_grey))
            // jika ingin warnanya berubah jadi abu-abu saat offline
        } else {
            btnPilih.alpha = 1.0f
            btnPilih.text = "Pilih"
        }

        btnPilih.setOnClickListener {
            if (isOnline) {
                val intentKePayment = Intent(this, PaymentActivity::class.java)
                intentKePayment.putExtra("PAY_NAMA", nama)
                intentKePayment.putExtra("PAY_SPEC", spec)
                intentKePayment.putExtra("PAY_HARGA", harga)
                intentKePayment.putExtra("PAY_GAMBAR", gambar)
                startActivity(intentKePayment)
            } else {
                Toast.makeText(this, "Maaf, dokter sedang offline.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}