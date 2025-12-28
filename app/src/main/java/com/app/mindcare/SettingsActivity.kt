package com.app.mindcare

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbarSettings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
            Toast.makeText(this, "Edit Profil (contoh)", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnNotifications).setOnClickListener {
            Toast.makeText(this, "Pengaturan notifikasi (contoh)", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnPrivacy).setOnClickListener {
            Toast.makeText(this, "Privasi & Keamanan (contoh)", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
