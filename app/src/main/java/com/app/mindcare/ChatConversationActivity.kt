package com.app.mindcare

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ChatConversationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_conversation)

        val toolbar = findViewById<Toolbar>(R.id.toolbarConv)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val doctor = intent.getStringExtra("doctor_name") ?: "Chat"
        supportActionBar?.title = doctor

        val rv = findViewById<RecyclerView>(R.id.rvMessages)
        rv.layoutManager = LinearLayoutManager(this)

        val et = findViewById<EditText>(R.id.etMessage)
        val btn = findViewById<Button>(R.id.btnSend)

        // Load messages
        val messages = loadMessages(doctor)
        val msgList = mutableListOf<JSONObject>()
        for (i in 0 until messages.length()) {
            messages.optJSONObject(i)?.let { msgList.add(it) }
        }

        val adapter = MessageAdapter(msgList)
        rv.adapter = adapter

        btn.setOnClickListener {
            val text = et.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val obj = JSONObject()
            obj.put("text", text)
            obj.put("time", sdf.format(Date()))
            obj.put("from", "user")

            messages.put(obj)
            saveMessages(doctor, messages)

            msgList.add(obj)
            adapter.notifyItemInserted(msgList.size - 1)
            rv.scrollToPosition(msgList.size - 1)
            et.setText("")
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
