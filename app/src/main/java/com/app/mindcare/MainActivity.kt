package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.mindcare.chat.ChatActivity
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

        // Menampilkan nama user atau email di header dashboard
        findViewById<TextView?>(R.id.tvUserName)?.text =
            user?.displayName ?: (user?.email ?: "MindCare User")

        // Navigasi ke fitur-fitur utama melalui tombol di Dashboard:
        
        // 1. Pilih Dokter
        findViewById<MaterialButton?>(R.id.btnChooseDoctor)?.setOnClickListener {
            val intent = Intent(this, PilihDokterActivity::class.java)
            startActivity(intent)
        }

        // 2. Chat
        findViewById<MaterialButton?>(R.id.btnChat)?.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        // 3. Janji Temu (Appointment)
        findViewById<MaterialButton?>(R.id.btnAppointment)?.setOnClickListener {
            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        // 4. Pembayaran (Payment)
        findViewById<MaterialButton?>(R.id.btnPayment)?.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }

        // 5. Tips Kesehatan Mental
        findViewById<MaterialButton?>(R.id.btnTips)?.setOnClickListener {
            startActivity(Intent(this, TipsActivity::class.java))
        }

        // ✅ Konfigurasi Bottom Navigation untuk navigasi antar layar utama
        findViewById<BottomNavigationView?>(R.id.bottomNav)?.apply {
            selectedItemId = R.id.nav_home
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> true // Tetap di Home
                    R.id.nav_chat -> {
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
        // Proteksi Halaman: Jika user tidak terdeteksi login, paksa kembali ke layar Login
        if (Firebase.auth.currentUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}
