package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Inisialisasi Firebase Auth jika belum
        if (!::auth.isInitialized) auth = Firebase.auth

        // Cek jika user sudah login, langsung arahkan ke Home (MainActivity)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        // Inisialisasi komponen UI
        val tilEmail = findViewById<TextInputLayout>(R.id.til_email)
        val etEmail = findViewById<TextInputEditText>(R.id.et_email)
        val tilPassword = findViewById<TextInputLayout>(R.id.til_password)
        val etPassword = findViewById<TextInputEditText>(R.id.et_password)

        val btnLogin = findViewById<MaterialButton>(R.id.btn_login)
        val tvRegister = findViewById<TextView>(R.id.tv_register_link)
        val tvForgot = findViewById<TextView>(R.id.tv_forgot_pass)

        // Logika saat tombol login ditekan
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            // Reset error message
            tilEmail.error = null
            tilPassword.error = null

            // Validasi input kosong
            if (email.isEmpty()) {
                tilEmail.error = "Email harus diisi"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                tilPassword.error = "Password harus diisi"
                return@setOnClickListener
            }

            // Proses login via Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Selamat datang di MindCare!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login gagal: cek email/password", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Navigasi ke halaman Register
        tvRegister.setOnClickListener {
            startActivity(Intent(this, DaftarAkun::class.java))
        }

        // Navigasi ke halaman Lupa Password
        tvForgot.setOnClickListener {
            startActivity(Intent(this, LupaPassword::class.java))
        }
    }
}