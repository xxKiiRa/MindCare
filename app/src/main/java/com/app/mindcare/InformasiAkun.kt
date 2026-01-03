package com.app.mindcare

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
    // Hapus lateinit btnLogout karena di XML activity_informasi_akun tidak ada tombol logout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informasi_akun)

        etNama = findViewById(R.id.et_nama)
        etEmail = findViewById(R.id.et_email)
        btnSave = findViewById(R.id.btn_save)

        // Tambahkan fungsi tombol back (Jika Anda ingin menambahkan tombol back di header)
        // findViewById<android.view.View?>(R.id.btnBack)?.setOnClickListener {
        //    onBackPressedDispatcher.onBackPressed()
        // }

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
        // Tombol Simpan
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
                            finish() // Kembali ke ProfileActivity
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
    }
}
