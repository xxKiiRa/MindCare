package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // 1. Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbarPayment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Konfirmasi Pembayaran"

        // 2. Hubungkan ke XML
        val img = findViewById<ImageView>(R.id.imgDoctorSelected)
        val tvName = findViewById<TextView>(R.id.tvSelectedName)
        val tvSpec = findViewById<TextView>(R.id.tvSelectedSpec)
        val tvPrice = findViewById<TextView>(R.id.tvSelectedPrice)
        val rgPayment = findViewById<RadioGroup>(R.id.rgPaymentMethods) // Tambahkan ini
        val btnPay = findViewById<Button>(R.id.btnPayNow)

        // 3. TANGKAP DATA (Pastikan di Adapter/Search kuncinya sama!)
        val nama = intent.getStringExtra("PAY_NAMA") ?: "-"
        val spec = intent.getStringExtra("PAY_SPEC") ?: "-"
        val harga = intent.getStringExtra("PAY_HARGA") ?: "Rp 0"
        val gambar = intent.getIntExtra("PAY_GAMBAR", R.drawable.docter_nimas)

        // 4. Tampilkan Data
        tvName.text = nama
        tvSpec.text = spec
        tvPrice.text = harga
        img.setImageResource(gambar)

        // 5. Klik Tombol Bayar
        btnPay.setOnClickListener {
            // Cek apakah user sudah pilih metode pembayaran
            val selectedMethodId = rgPayment.checkedRadioButtonId

            if (selectedMethodId == -1) {
                // Jika belum pilih sama sekali
                Toast.makeText(this, "Silakan pilih metode pembayaran dahulu!", Toast.LENGTH_SHORT).show()
            } else {
                // Jika sudah pilih
                val selectedRb = findViewById<RadioButton>(selectedMethodId)
                val namaMetode = selectedRb.text.toString()

                Toast.makeText(this, "Pembayaran Berhasil via $namaMetode", Toast.LENGTH_LONG).show()

                // Pindah ke Chat
                val i = Intent(this, ChatConversationActivity::class.java)
                i.putExtra("doctor_name", nama)
                startActivity(i)

                finish() // Tutup halaman payment agar tidak bisa back ke sini lagi
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}