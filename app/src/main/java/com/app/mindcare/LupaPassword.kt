package com.app.mindcare

import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LupaPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_password)

        // Inisialisasi komponen UI
        val etEmail = findViewById<TextInputEditText>(R.id.et_email_lupa)
        val btnReset = findViewById<MaterialButton>(R.id.btn_reset)
        val tvBack = findViewById<TextView>(R.id.tv_back_login)

        // Logika saat tombol reset password ditekan
        btnReset.setOnClickListener {
            val email = etEmail.text.toString().trim()

            // Validasi Input: Email tidak boleh kosong
            if (email.isEmpty()) {
                etEmail.error = "Email wajib diisi"
                return@setOnClickListener
            }
            // Validasi Input: Format email harus sesuai standar
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Format email salah"
                return@setOnClickListener
            }

            // Mengirim instruksi reset password ke email yang didaftarkan melalui Firebase
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Jika berhasil, beri tahu pengguna untuk mengecek kotak masuk emailnya
                        Toast.makeText(this, "Link reset dikirim. Cek email kamu.", Toast.LENGTH_LONG).show()
                        finish() // Kembali ke halaman login
                    } else {
                        // Menampilkan pesan error jika gagal mengirim email reset
                        Toast.makeText(this, "Gagal: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Kembali ke halaman Login tanpa melakukan apa pun
        tvBack.setOnClickListener { finish() }
    }
}