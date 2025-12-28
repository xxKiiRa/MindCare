package com.app.mindcare

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val toolbar = findViewById<Toolbar>(R.id.toolbarPayment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Summary views
        val img = findViewById<ImageView>(R.id.imgDoctorSelected)
        val tvName = findViewById<TextView>(R.id.tvSelectedName)
        val tvSpec = findViewById<TextView>(R.id.tvSelectedSpec)
        val tvPrice = findViewById<TextView>(R.id.tvSelectedPrice)

        // Read extras
        val name = intent.getStringExtra("NAMA_DOKTER") ?: "-"
        val spec = intent.getStringExtra("SPESIALIS") ?: "-"
        val price = intent.getStringExtra("HARGA") ?: "Rp 0"
        val imgRes = intent.getIntExtra("GAMBAR", R.drawable.docter1)

        img.setImageResource(imgRes)
        tvName.text = name
        tvSpec.text = spec
        tvPrice.text = price

        val rg = findViewById<RadioGroup>(R.id.rgPaymentMethods)

        findViewById<Button>(R.id.btnPayNow).setOnClickListener {
            val selectedId = rg.checkedRadioButtonId
            val method = when (selectedId) {
                R.id.rbCard -> "Kartu"
                R.id.rbBank -> "Bank"
                R.id.rbEwallet -> "E-wallet"
                else -> "Tidak dipilih"
            }

            // Save payment record to SharedPreferences as JSON array
            savePaymentRecord(this, name, spec, price, method)

            Toast.makeText(this, "Pembayaran berhasil via $method", Toast.LENGTH_SHORT).show()

            // After payment, go to History (or finish)
            finish()
        }
    }

    private fun savePaymentRecord(context: Context, name: String, spec: String, price: String, method: String) {
        val prefs = context.getSharedPreferences("mindcare_prefs", Context.MODE_PRIVATE)
        val raw = prefs.getString("payment_history", null)
        val arr = if (raw != null) JSONArray(raw) else JSONArray()

        val obj = JSONObject()
        obj.put("doctor", name)
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
