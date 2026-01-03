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

        //   nama user di header
        findViewById<TextView?>(R.id.tvUserName)?.text =
            user?.displayName ?: (user?.email ?: "MindCare User")

        //  Tombol menu Home (sementara diarahkan / toast)

        findViewById<MaterialButton?>(R.id.btnChooseDoctor)?.setOnClickListener {

            val intent = Intent(this, PilihDokterActivity::class.java)

            startActivity(intent)
        }

        findViewById<MaterialButton?>(R.id.btnChat)?.setOnClickListener {

            startActivity(Intent(this, ChatActivity::class.java))
        }

        findViewById<MaterialButton?>(R.id.btnAppointment)?.setOnClickListener {

            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        findViewById<MaterialButton?>(R.id.btnPayment)?.setOnClickListener {

            startActivity(Intent(this, PaymentActivity::class.java))
        }

        findViewById<MaterialButton?>(R.id.btnTips)?.setOnClickListener {

            startActivity(Intent(this, TipsActivity::class.java))
        }

        // ✅ Bottom Nav
        findViewById<BottomNavigationView?>(R.id.bottomNav)?.apply {
            selectedItemId = R.id.nav_home
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> true
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
        // no no masuk kalau blm login
        if (Firebase.auth.currentUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}
