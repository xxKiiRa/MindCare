package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val toolbar = findViewById<Toolbar>(R.id.toolbarCart)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val img = findViewById<ImageView>(R.id.imgCartDoctor)
        val tvName = findViewById<TextView>(R.id.tvCartName)
        val tvSpec = findViewById<TextView>(R.id.tvCartSpec)
        val tvPrice = findViewById<TextView>(R.id.tvCartPrice)

        // get data from intent
        val name = intent.getStringExtra("NAMA_DOKTER") ?: "-"
        val spec = intent.getStringExtra("SPESIALIS") ?: "-"
        val price = intent.getStringExtra("HARGA") ?: "-"
        val imgRes = intent.getIntExtra("GAMBAR", R.drawable.docter1)

        img.setImageResource(imgRes)
        tvName.text = name
        tvSpec.text = spec
        tvPrice.text = price

        // button lanjut ke pembayaran
        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            val i = Intent(this, PaymentActivity::class.java)
            i.putExtra("NAMA_DOKTER", name)
            i.putExtra("SPESIALIS", spec)
            i.putExtra("HARGA", price)
            i.putExtra("GAMBAR", imgRes)
            startActivity(i)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

