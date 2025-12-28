package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        val user = auth.currentUser

        // ✅ Set nama user di header (kalau id-nya ada)
        findViewById<TextView?>(R.id.tvUserName)?.text =
            user?.displayName ?: (user?.email ?: "MindCare User")

        // ✅ Tombol menu Home (sementara diarahkan / toast)
        // Cari tombolnya
        findViewById<MaterialButton?>(R.id.btnChooseDoctor)?.setOnClickListener {
            // 1. Buat Intent untuk pindah ke PilihDokterActivity
            val intent = Intent(this, PilihDokterActivity::class.java)

            // 2. Jalankan perpindahan halaman
            startActivity(intent)
        }

        findViewById<MaterialButton?>(R.id.btnChat)?.setOnClickListener {
            // Open ChatActivity instead of showing a Toast
            startActivity(Intent(this, ChatActivity::class.java))
        }

        findViewById<MaterialButton?>(R.id.btnAppointment)?.setOnClickListener {
            // Open AppointmentActivity
            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        findViewById<MaterialButton?>(R.id.btnPayment)?.setOnClickListener {
            // Open PaymentActivity
            startActivity(Intent(this, PaymentActivity::class.java))
        }

        findViewById<MaterialButton?>(R.id.btnTips)?.setOnClickListener {
            // Open TipsActivity
            startActivity(Intent(this, TipsActivity::class.java))
        }

        // ✅ Bottom Nav
        findViewById<BottomNavigationView?>(R.id.bottomNav)?.apply {
            selectedItemId = R.id.nav_home
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> true
                    R.id.nav_chat -> {
                        // Navigate to ChatActivity when tapping bottom nav
                        startActivity(Intent(this@MainActivity, ChatActivity::class.java))
                        true
                    }
                    R.id.nav_profile -> {
                        startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // ✅ Cegah masuk home kalau belum login
        if (Firebase.auth.currentUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}
