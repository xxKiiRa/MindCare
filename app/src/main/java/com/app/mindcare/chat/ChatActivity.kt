package com.app.mindcare.chat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.mindcare.chat.ChatConversationActivity
import com.app.mindcare.R

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val toolbar = findViewById<Toolbar>(R.id.toolbarChat)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val btnStart = findViewById<Button>(R.id.btnStartChat)
        btnStart?.setOnClickListener {
            Toast.makeText(this, "Memulai chat baru... ", Toast.LENGTH_SHORT).show()
        }

        // Use the container IDs directly
        val item1 = findViewById<LinearLayout>(R.id.chatItem1)
        val name1 = findViewById<TextView>(R.id.tvChatName1)
        item1?.setOnClickListener {
            startActivity(Intent(this, ChatConversationActivity::class.java).apply {
                putExtra("doctor_name", name1.text.toString())
            })
        }

        val item2 = findViewById<LinearLayout>(R.id.chatItem2)
        val name2 = findViewById<TextView>(R.id.tvChatName2)
        item2?.setOnClickListener {
            startActivity(Intent(this, ChatConversationActivity::class.java).apply {
                putExtra("doctor_name", name2.text.toString())
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Use OnBackPressedDispatcher instead of deprecated onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}