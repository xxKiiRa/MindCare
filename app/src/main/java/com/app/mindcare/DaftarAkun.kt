package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth

class DaftarAkun : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_akun)

        // Inisialisasi Firebase Auth
        auth = Firebase.auth

        val etNama = findViewById<TextInputEditText>(R.id.et_nama)
        val etEmail = findViewById<TextInputEditText>(R.id.et_email_daftar)
        val etPassword = findViewById<TextInputEditText>(R.id.et_pass_daftar)
        val btnDaftar = findViewById<MaterialButton>(R.id.btn_daftar)
        val tvLogin = findViewById<TextView>(R.id.tv_login_link)

        // Logika pendaftaran saat tombol daftar ditekan
        btnDaftar.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            // Validasi Input: Nama tidak boleh kosong
            if (nama.isEmpty()) {
                etNama.error = "Nama lengkap wajib diisi"
                etNama.requestFocus()
                return@setOnClickListener
            }
            // Validasi Input: Format email harus benar
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Email tidak valid"
                etEmail.requestFocus()
                return@setOnClickListener
            }
            // Validasi Input: Password minimal 6 karakter (syarat Firebase)
            if (password.length < 6) {
                etPassword.error = "Password minimal 6 karakter"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // Membuat user baru di Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Jika berhasil, update profil user untuk menyimpan Nama Lengkap (Display Name)
                        val user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(nama)
                            .build()

                        user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                            Toast.makeText(this, "Pendaftaran berhasil. Silakan login.", Toast.LENGTH_SHORT).show()
                            // Sign out otomatis setelah daftar agar user login manual dari halaman Login
                            auth.signOut()
                            startActivity(Intent(this, Login::class.java))
                            finishAffinity()
                        }
                    } else {
                        // Menampilkan pesan error jika gagal (misal: email sudah terdaftar)
                        Toast.makeText(this, "Gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Kembali ke halaman Login
        tvLogin.setOnClickListener { finish() }
    }
}