package com.app.mindcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.mindcare.chat.ChatConversationActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbarPayment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pembayaran"

        // View doctor summary
        val img = findViewById<ImageView>(R.id.imgDoctorSelected)
        val tvName = findViewById<TextView>(R.id.tvSelectedName)
        val tvSpec = findViewById<TextView>(R.id.tvSelectedSpec)
        val tvPrice = findViewById<TextView>(R.id.tvSelectedPrice)

        // Read data from intent
        val name = intent.getStringExtra("NAMA_DOKTER") ?: "-"
        val spec = intent.getStringExtra("SPESIALIS") ?: "-"
        val price = intent.getStringExtra("HARGA") ?: "-"
        val imgRes = intent.getIntExtra("GAMBAR", R.drawable.docter1)

        img.setImageResource(imgRes)
        tvName.text = name
        tvSpec.text = spec
        tvPrice.text = price

        // Payment method radio group
        val rg = findViewById<RadioGroup>(R.id.rgPaymentMethods)

        // PAY BUTTON
        findViewById<Button>(R.id.btnPayNow).setOnClickListener {
            val selectedId = rg.checkedRadioButtonId
            val method = when (selectedId) {
                R.id.rbCard -> "Kartu Kredit/Debit"
                R.id.rbBank -> "Bank Transfer"
                R.id.rbEwallet -> "E-Wallet"
                else -> "Tidak Dipilih"
            }

            if (method == "Tidak Dipilih") {
                Toast.makeText(this, "Pilih metode pembayaran!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save payment history
            savePaymentRecord(this, name, spec, price, method)

            Toast.makeText(this, "Pembayaran berhasil via $method", Toast.LENGTH_LONG).show()

            // Buka chat langsung setelah pembayaran
            val i = Intent(this, ChatConversationActivity::class.java)
            i.putExtra("doctor_name", name)
            i.putExtra("can_chat", true)
            startActivity(i)

            finish() // tutup PaymentActivity
        }
    }

    private fun savePaymentRecord(
        context: Context,
        doctor: String,
        spec: String,
        price: String,
        method: String
    ) {
        val prefs = context.getSharedPreferences("mindcare_prefs", Context.MODE_PRIVATE)
        val raw = prefs.getString("payment_history", null)
        val arr = if (raw != null) JSONArray(raw) else JSONArray()

        val obj = JSONObject()
        obj.put("doctor", doctor)
        obj.put("spec", spec)
        obj.put("price", price)
        obj.put("method", method)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        obj.put("time", sdf.format(Date()))

        arr.put(obj)

        prefs.edit().putString("payment_history", arr.toString()).apply()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
