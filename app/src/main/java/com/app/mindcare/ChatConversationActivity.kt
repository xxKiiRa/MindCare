package com.app.mindcare

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mindcare.chat.MessageAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ChatConversationActivity : AppCompatActivity() {

    // Daftar Template: Key = Pesan Pasien, Value = Balasan Otomatis Psikiater
    private val chatTemplates = mapOf(
        "Halo Dok, saya ingin konsultasi" to "Halo, tentu. Silakan ceritakan keluhan yang Anda rasakan saat ini.",
        "Bagaimana cara minum obatnya?" to "Obat diminum 1x sehari setelah makan malam ya. Jangan lupa istirahat cukup.",
        "Apakah efek samping obat ini?" to "Efek samping umum adalah rasa kantuk ringan. Jika ada gejala lain, segera hubungi saya.",
        "Terima kasih Dok" to "Sama-sama, semoga lekas membaik dan sehat selalu!",
        "Kapan jadwal kontrol lagi?" to "Jadwal kontrol berikutnya disarankan 1 minggu dari sekarang."
    )

    private lateinit var rv: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var et: EditText
    private lateinit var messages: JSONArray
    private lateinit var doctorName: String
    private val msgList = mutableListOf<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_conversation)

        val toolbar = findViewById<Toolbar>(R.id.toolbarConv)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        doctorName = intent.getStringExtra("doctor_name") ?: "Chat"
        supportActionBar?.title = doctorName

        rv = findViewById(R.id.rvMessages)
        et = findViewById(R.id.etMessage)
        val btn = findViewById<Button>(R.id.btnSend)
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupTemplates)

        // Setup RecyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true // Pesan terbaru di bawah
        rv.layoutManager = layoutManager

        // Load messages
        messages = loadMessages(doctorName)
        for (i in 0 until messages.length()) {
            messages.optJSONObject(i)?.let { msgList.add(it) }
        }

        adapter = MessageAdapter(msgList)
        rv.adapter = adapter
        scrollToBottom()

        // Setup Template Chips
        setupChatTemplates(chipGroup)

        // Kirim Manual
        btn.setOnClickListener {
            val text = et.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text, "user")
                et.setText("")

                // Opsional: Jika ingin bot tetap membalas pesan manual dengan jawaban default
                // autoReply("Maaf saya sedang menangani pasien lain, akan saya balas sebentar lagi.")
            }
        }
    }

    private fun setupChatTemplates(chipGroup: ChipGroup) {
        for ((question, answer) in chatTemplates) {
            val chip = Chip(this)
            chip.text = question
            chip.isCheckable = false
            chip.setOnClickListener {
                // 1. Kirim pesan Pasien
                sendMessage(question, "user")

                // 2. Simulasikan Psikiater mengetik/membalas otomatis
                showTypingAndReply(answer)
            }
            chipGroup.addView(chip)
        }
    }

    private fun showTypingAndReply(replyText: String) {
        // Delay 1.5 detik seolah-olah dokter sedang membalas
        Handler(Looper.getMainLooper()).postDelayed({
            sendMessage(replyText, "doctor")
        }, 1500)
    }

    private fun sendMessage(text: String, role: String) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val obj = JSONObject()
        obj.put("text", text)
        obj.put("time", sdf.format(Date()))
        obj.put("from", role) // 'user' atau 'doctor'

        // Simpan ke SharedPreferences
        messages.put(obj)
        saveMessages(doctorName, messages)

        // Update UI
        msgList.add(obj)
        adapter.notifyItemInserted(msgList.size - 1)
        scrollToBottom()
    }

    private fun scrollToBottom() {
        if (msgList.isNotEmpty()) {
            rv.smoothScrollToPosition(msgList.size - 1)
        }
    }

    private fun messagesKey(doctor: String) = "chat_" + doctor.replace(" ", "_")

    private fun loadMessages(doctor: String): JSONArray {
        val prefs = getSharedPreferences("mindcare_prefs", MODE_PRIVATE)
        val raw = prefs.getString(messagesKey(doctor), null)
        return if (raw != null) JSONArray(raw) else JSONArray()
    }

    private fun saveMessages(doctor: String, arr: JSONArray) {
        val prefs = getSharedPreferences("mindcare_prefs", MODE_PRIVATE)
        prefs.edit().putString(messagesKey(doctor), arr.toString()).apply()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}