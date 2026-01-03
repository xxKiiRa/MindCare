package com.app.mindcare.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mindcare.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.collections.iterator

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

    // Use ChatMessage model for UI list
    private val msgList = mutableListOf<ChatMessage>()

    // Identifier for the current user in messages (matches stored 'from' field)
    private val myUid = "user"

    // Flag whether user is allowed to chat (set from intent)
    private var canChat = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_conversation)

        val toolbar = findViewById<Toolbar>(R.id.toolbarConv)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Add long-press on toolbar to show saved messages JSON for debugging
        toolbar.setOnLongClickListener {
            showSavedMessagesDialog()
            true
        }

        doctorName = intent.getStringExtra("doctor_name") ?: "Chat"
        supportActionBar?.title = doctorName

        // Optional flag from caller: apakah pengguna sudah bayar dan boleh chat?
        canChat = intent.getBooleanExtra("can_chat", true)

        rv = findViewById(R.id.rvMessages)
        et = findViewById(R.id.etMessage)
        val btn = findViewById<Button>(R.id.btnSend)
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupTemplates)

        // Jika tidak boleh chat, disable input dan tombol
        if (!canChat) {
            et.isEnabled = false
            et.hint = "Selesaikan pembayaran untuk memulai konsultasi"
            btn.isEnabled = false
        }

        // Setup RecyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true // Pesan terbaru di bawah
        rv.layoutManager = layoutManager

        // Normalize and show the storage key we will use
        val key = messagesKey(doctorName)
        Log.d("ChatConv", "using messagesKey='$key' for doctorName='$doctorName'")
        Toast.makeText(this, "Chat key: $key", Toast.LENGTH_SHORT).show()

        // Load messages (persisted as JSONArray) and convert to ChatMessage untuk adapter
        messages = loadMessages(doctorName)
        for (i in 0 until messages.length()) {
            messages.optJSONObject(i)?.let { json ->
                val text = json.optString("text")
                val from = json.optString("from")
                val time = json.optString("time")
                // Use time+index as a simple stable id
                val id = "$time-$i"
                val chatMsg = ChatMessage(id = id, senderId = from, text = text, createdAt = null)
                msgList.add(chatMsg)
            }
        }

        Log.d("ChatConv", "onCreate loaded messages for '$doctorName': ${msgList.size}")

        adapter = MessageAdapter(myUid)
        rv.adapter = adapter
        // Submit a copy of the list to the ListAdapter and scroll after commit
        adapter.submitList(ArrayList(msgList)) {
            Log.d("ChatConv", "initial submitList committed, size=${adapter.currentList.size}")
            rv.post {
                if (adapter.currentList.size > 0) rv.scrollToPosition(adapter.currentList.size - 1)
            }
        }
        scrollToBottom()

        // Setup Template Chips
        setupChatTemplates(chipGroup)

        // Kirim Manual
        btn.setOnClickListener {
            if (!canChat) {
                Toast.makeText(this, "Selesaikan pembayaran terlebih dahulu untuk berkonsultasi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val text = et.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text, "user")
                et.setText("")
            } else {
                Toast.makeText(this, "Tulis pesan terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupChatTemplates(chipGroup: ChipGroup) {
        chipGroup.removeAllViews()
        for ((question, answer) in chatTemplates) {
            val chip = Chip(this)
            chip.text = question
            chip.isCheckable = false
            chip.isEnabled = canChat
            chip.alpha = if (canChat) 1f else 0.5f
            chip.setOnClickListener {
                if (!canChat) {
                    Toast.makeText(this, "Selesaikan pembayaran terlebih dahulu untuk berkonsultasi", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
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
        if (!canChat) return

        Log.d("ChatConv", "sendMessage called with text='$text' role='$role'")

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeStr = sdf.format(Date())
        val obj = JSONObject()
        obj.put("text", text)
        obj.put("time", timeStr)
        obj.put("from", role) // 'user' atau 'doctor'

        // Simpan ke SharedPreferences (lakukan sebelum update UI)
        messages.put(obj)
        saveMessages(doctorName, messages)

        // Quick debug: read back saved messages count
        try {
            val prefs = getSharedPreferences("mindcare_prefs", MODE_PRIVATE)
            val raw = prefs.getString(messagesKey(doctorName), null)
            val cnt = if (raw != null) JSONArray(raw).length() else 0
            // show a short toast for debug so user can see messages saved
            Toast.makeText(this, "Saved messages: $cnt", Toast.LENGTH_SHORT).show()
            Log.d("ChatConv", "after save, stored count=$cnt")
        } catch (t: Throwable) {
            Log.e("ChatConv", "error reading saved messages", t)
        }

        // Update UI (convert to ChatMessage and submit to adapter)
        val id = UUID.randomUUID().toString()
        val chatMsg = ChatMessage(id = id, senderId = role, text = text, createdAt = null)
        msgList.add(chatMsg)

        Log.d("ChatConv", "msgList size before submit=${msgList.size}")

        // Pastikan update adapter di UI thread
        rv.post {
            adapter.submitList(ArrayList(msgList)) {
                Log.d("ChatConv", "submitList committed, adapter currentList size=${adapter.currentList.size}")
                rv.post {
                    if (msgList.size > 0) rv.scrollToPosition(msgList.size - 1)
                }
            }
        }

        // Feedback singkat
        Toast.makeText(this, "Pesan terkirim", Toast.LENGTH_SHORT).show()
    }

    private fun scrollToBottom() {
        if (adapter.itemCount > 0) {
            rv.post { rv.smoothScrollToPosition(adapter.itemCount - 1) }
        }
    }

    private fun messagesKey(doctor: String): String {
        // normalize: trim, lower-case, replace non-alphanumeric with underscore
        val normalized = doctor.trim().lowercase(Locale.ROOT).replace("\\s+".toRegex(), "_")
            .replace("[^a-z0-9_]".toRegex(), "_")
        return "chat_" + normalized
    }

    private fun legacyMessagesKey(doctor: String): String {
        // previous format used in older app versions
        return "chat_" + doctor.replace(" ", "_")
    }

    private fun loadMessages(doctor: String): JSONArray {
        val prefs = getSharedPreferences("mindcare_prefs", MODE_PRIVATE)
        val normKey = messagesKey(doctor)
        val raw = prefs.getString(normKey, null)
        if (raw != null) {
            Log.d("ChatConv", "loadMessages using normalized key='$normKey' found")
            return JSONArray(raw)
        }

        // try legacy key
        val legacyKey = legacyMessagesKey(doctor)
        val rawLegacy = prefs.getString(legacyKey, null)
        if (rawLegacy != null) {
            Log.d("ChatConv", "loadMessages found legacy key='$legacyKey', migrating to '$normKey'")
            // migrate to normalized key
            prefs.edit {
                putString(normKey, rawLegacy)
                remove(legacyKey)
            }
            return JSONArray(rawLegacy)
        }

        // try unprefixed raw doctor name key (very old possibility)
        val alt = prefs.getString(doctor, null)
        if (alt != null) {
            Log.d("ChatConv", "loadMessages found alt key='$doctor', migrating to '$normKey'")
            prefs.edit {
                putString(normKey, alt)
                remove(doctor)
            }
            return JSONArray(alt)
        }

        Log.d("ChatConv", "loadMessages no existing messages for '$doctor' (keys tried: $normKey, $legacyKey)")
        return JSONArray()
    }

    private fun saveMessages(doctor: String, arr: JSONArray) {
        val prefs = getSharedPreferences("mindcare_prefs", MODE_PRIVATE)
        val key = messagesKey(doctor)
        val legacyKey = legacyMessagesKey(doctor)
        Log.d("ChatConv", "saveMessages using key='$key' size=${arr.length()}, removing legacyKey='$legacyKey'")
        prefs.edit {
            putString(key, arr.toString())
            remove(legacyKey)
            remove(doctor)
        }
        Toast.makeText(this, "Saved under key: $key", Toast.LENGTH_SHORT).show()
    }

    private fun showSavedMessagesDialog() {
        try {
            val prefs = getSharedPreferences("mindcare_prefs", MODE_PRIVATE)
            val key = messagesKey(doctorName)
            val raw = prefs.getString(key, null) ?: ""
            val msg = if (raw.isBlank()) "(no messages stored)" else raw
            AlertDialog.Builder(this)
                .setTitle("Saved messages for $doctorName")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show()
        } catch (t: Throwable) {
            Toast.makeText(this, "Error reading saved messages: ${t.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}