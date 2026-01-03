package com.app.mindcare

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

import java.io.ByteArrayOutputStream

class InformasiAkun : AppCompatActivity() {

    private lateinit var etNama: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var ivProfile: ImageView

    private val db = FirebaseFirestore.getInstance()
    private var imageUri: Uri? = null

    // === Gallery Picker (NEW API) ===
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                ivProfile.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informasi_akun)

        etNama = findViewById(R.id.et_nama)
        etEmail = findViewById(R.id.et_email)
        btnSave = findViewById(R.id.btn_save)
        ivProfile = findViewById(R.id.iv_profile)

        ivProfile.setOnClickListener {
            pickImage.launch("image/*")
        }

        loadUserData()
        setupAction()
    }

    // ================= LOAD USER =================
    private fun loadUserData() {
        val user = Firebase.auth.currentUser ?: return

        etNama.setText(user.displayName)
        etEmail.setText(user.email)

        db.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                val base64 = doc.getString("photoBase64")
                if (!base64.isNullOrEmpty()) {
                    val bytes = Base64.decode(base64, Base64.DEFAULT)
                    Glide.with(this)
                        .load(bytes)
                        .into(ivProfile)
                }
            }
    }

    // ================= SAVE =================
    private fun setupAction() {
        btnSave.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val user = Firebase.auth.currentUser ?: return@setOnClickListener

            if (nama.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            }

            updateAuthProfile(user.uid, nama)
        }
    }

    // ================= UPDATE AUTH =================
    private fun updateAuthProfile(uid: String, nama: String) {
        Firebase.auth.currentUser!!
            .updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(nama)
                    .build()
            )
            .addOnSuccessListener {
                saveToFirestore(uid, nama)
            }
            .addOnFailureListener { e ->
                showError("Gagal update auth: ${e.message}")
            }
    }

    // ================= FIRESTORE =================
    private fun saveToFirestore(uid: String, nama: String) {
        val data = hashMapOf<String, Any>(
            "nama" to nama,
            "email" to etEmail.text.toString()
        )

        imageUri?.let {
            try {
                val base64 = uriToBase64(it)
                data["photoBase64"] = base64
            } catch (e: Exception) {
                showError("Gagal konversi gambar")
                return
            }
        }

        db.collection("users").document(uid)
            .set(data, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                AlertDialog.Builder(this)
                    .setTitle("Berhasil")
                    .setMessage("Profil berhasil diperbarui")
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .show()
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", e.message ?: "")
                showError("Gagal simpan Firestore: ${e.message}")
            }
    }

    // ================= IMAGE TO BASE64 =================
    private fun uriToBase64(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw Exception("InputStream null")

        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Resize kecil (AMAN untuk Firestore)
        val resized = Bitmap.createScaledBitmap(bitmap, 300, 300, true)

        val output = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 40, output)

        return Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
    }

    // ================= ERROR =================
    private fun showError(msg: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(msg)
            .setPositiveButton("OK", null)
            .show()
    }
}
