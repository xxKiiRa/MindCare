package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val user = Firebase.auth.currentUser

        // Aman walau ID belum ada
        findViewById<TextView?>(R.id.tvProfileName)?.text =
            user?.displayName ?: (user?.email ?: "MindCare User")

        findViewById<TextView?>(R.id.tvProfileEmail)?.text =
            user?.email ?: "—"

        findViewById<MaterialButton?>(R.id.btnLogoutProfile)?.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        //-----------------------------Tombol informasi akun (Rindra)-------------------------------

        findViewById<MaterialButton?>(R.id.btnInfoUser)?.setOnClickListener {
            startActivity(Intent(this, InformasiAkun::class.java))
        }
        //__________________________________________________________________________________________


        findViewById<MaterialButton?>(R.id.btnHistory)?.setOnClickListener {
            Toast.makeText(this, "Riwayat Konsultasi (soon)", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton?>(R.id.btnSettings)?.setOnClickListener {
            Toast.makeText(this, "Settings (soon)", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton?>(R.id.btnHelp)?.setOnClickListener {
            Toast.makeText(this, "FAQ (soon)", Toast.LENGTH_SHORT).show()
        }
    }
}
