package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
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
            Toast.makeText(this, "Chat Konsultasi (soon)", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, ChatListActivity::class.java))
        }

        findViewById<MaterialButton?>(R.id.btnAppointment)?.setOnClickListener {
            Toast.makeText(this, "Jadwal Temu (soon)", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, AppointmentActivity::class.java))
        }

        findViewById<MaterialButton?>(R.id.btnPayment)?.setOnClickListener {
            Toast.makeText(this, "Pembayaran (soon)", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, PaymentActivity::class.java))
        }

        findViewById<MaterialButton?>(R.id.btnTips)?.setOnClickListener {
            Toast.makeText(this, "Tips Kesehatan Mental (soon)", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, TipsActivity::class.java))
        }

        // ✅ Bottom Nav
        findViewById<BottomNavigationView?>(R.id.bottomNav)?.apply {
            selectedItemId = R.id.nav_home
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> true
                    R.id.nav_chat -> {
                        Toast.makeText(this@MainActivity, "Chat (soon)", Toast.LENGTH_SHORT).show()
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
