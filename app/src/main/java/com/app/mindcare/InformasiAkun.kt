
package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth

class InformasiAkun : AppCompatActivity() {

    private lateinit var etNama: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informasi_akun)

        etNama = findViewById(R.id.et_nama)
        etEmail = findViewById(R.id.et_email)
        btnSave = findViewById(R.id.btn_save)
        btnLogout = findViewById(R.id.btn_logout)

        loadUserData()
        setupAction()
    }

    private fun loadUserData() {
        val user = Firebase.auth.currentUser

        if (user != null) {
            etNama.setText(user.displayName)
            etEmail.setText(user.email)
        }
    }

    private fun setupAction() {

        // tombol untuk Simpan nama lu
        btnSave.setOnClickListener {
            val namaBaru = etNama.text.toString().trim()

            if (namaBaru.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            }

            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(namaBaru)
                .build()

            Firebase.auth.currentUser
                ?.updateProfile(profileUpdate)
                ?.addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("Berhasil")
                        .setMessage("Profil berhasil diperbarui")
                        .setPositiveButton("OK") { _, _ ->
                            finish() // balik ke ProfileActivity.kt kyknya
                        }
                        .show()
                }
                ?.addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setTitle("Gagal")
                        .setMessage("Gagal memperbarui profil")
                        .setPositiveButton("OK", null)
                        .show()
                }
        }

        //tombol logout (sementara dlu)
        btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah kamu yakin ingin logout?")
                .setPositiveButton("Ya") { _, _ ->
                    Firebase.auth.signOut()
                    startActivity(Intent(this, Login::class.java))
                    finishAffinity()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }
}
