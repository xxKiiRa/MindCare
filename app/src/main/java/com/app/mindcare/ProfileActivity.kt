package com.app.mindcare

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val user = Firebase.auth.currentUser

        val tvName = findViewById<TextView>(R.id.tvProfileName)
        val tvEmail = findViewById<TextView>(R.id.tvProfileEmail)
        val ivProfile = findViewById<ImageView>(R.id.ivProfile)

        tvName.text = user?.displayName ?: "MindCare User"
        tvEmail.text = user?.email ?: "—"

        // 🔥 LOAD FOTO DARI FIRESTORE (BASE64)
        user?.uid?.let { uid ->
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    val base64 = doc.getString("photoBase64")
                    if (!base64.isNullOrEmpty()) {
                        val bytes = Base64.decode(base64, Base64.DEFAULT)
                        Glide.with(this)
                            .load(bytes)
                            .placeholder(R.drawable.baseline_account_circle_24)
                            .into(ivProfile)
                    }
                }
        }

        findViewById<View>(R.id.btnLogout).setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        findViewById<View>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<View>(R.id.menuPersonalInfo).setOnClickListener {
            startActivity(Intent(this, InformasiAkun::class.java))
        }

        findViewById<View>(R.id.menuHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<View>(R.id.menuHelp).setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }
    }
}
