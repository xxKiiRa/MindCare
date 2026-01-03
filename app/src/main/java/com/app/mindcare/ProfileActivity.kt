package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val user = Firebase.auth.currentUser

        // Menggunakan View? agar lebih aman jika tipe di XML berbeda (TextView/Button/Layout)
        findViewById<TextView?>(R.id.tvProfileName)?.text =
            user?.displayName ?: (user?.email ?: "MindCare User")

        findViewById<TextView?>(R.id.tvProfileEmail)?.text =
            user?.email ?: "—"

        findViewById<View?>(R.id.btnLogout)?.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        findViewById<View?>(R.id.btnBack)?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() //
        }

        //-----------------------------Tombol informasi akun (Rindra)-------------------------------
        // PERBAIKAN: Menggunakan View? sebagai pengganti MaterialButton? agar tidak ClassCastException
        findViewById<View?>(R.id.menuPersonalInfo)?.setOnClickListener {
            startActivity(Intent(this, InformasiAkun::class.java))
        }
        //__________________________________________________________________________________________

        findViewById<View?>(R.id.menuHistory)?.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<View?>(R.id.menuHelp)?.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }
    }
}
