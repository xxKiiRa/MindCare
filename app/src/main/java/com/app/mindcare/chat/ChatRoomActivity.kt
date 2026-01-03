package com.app.mindcare.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mindcare.databinding.ActivityChatRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var b: ActivityChatRoomBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var chatId: String
    private lateinit var otherUserId: String

    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(b.root)

        chatId = intent.getStringExtra("chatId") ?: ""
        otherUserId = intent.getStringExtra("otherUserId") ?: ""
        if (chatId.isBlank()) {
            Toast.makeText(this, "ChatId kosong", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        adapter = MessageAdapter(myUid = auth.currentUser?.uid ?: "")
        b.rvMessages.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        b.rvMessages.adapter = adapter

        b.btnBack.setOnClickListener { finish() }

        b.btnSend.setOnClickListener { sendMessage() }

        listenMessages()
    }

    private fun listenMessages() {
        db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, _ ->
                val msgs = snap?.documents?.map { d ->
                    val m = d.toObject(ChatMessage::class.java) ?: ChatMessage()
                    m.copy(id = d.id)
                } ?: emptyList()

                adapter.submitList(msgs)
                if (msgs.isNotEmpty()) b.rvMessages.scrollToPosition(msgs.size - 1)
            }
    }

    private fun sendMessage() {
        val myId = auth.currentUser?.uid ?: return
        val text = b.etMessage.text?.toString()?.trim().orEmpty()
        if (text.isBlank()) return

        b.etMessage.setText("")

        val msgData = hashMapOf(
            "senderId" to myId,
            "text" to text,
            "createdAt" to FieldValue.serverTimestamp()
        )

        val chatRef = db.collection("chats").document(chatId)
        val msgRef = chatRef.collection("messages").document()

        db.runBatch { batch ->
            batch.set(msgRef, msgData)

            // update room meta
            batch.set(chatRef, mapOf(
                "lastMessage" to text,
                "lastSenderId" to myId,
                "updatedAt" to FieldValue.serverTimestamp()
            ), SetOptions.merge())
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengirim: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}