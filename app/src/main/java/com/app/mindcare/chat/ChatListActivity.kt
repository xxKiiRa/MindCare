package com.app.mindcare.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mindcare.databinding.ActivityChatListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatListActivity : AppCompatActivity() {

    private lateinit var b: ActivityChatListBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth

    private val adapter = ChatRoomAdapter { chat ->
        // buka chat room, tentukan lawan chat (doctor/patient)
        val myId = auth.currentUser?.uid ?: return@ChatRoomAdapter
        val otherId = if (chat.patientId == myId) chat.doctorId else chat.patientId

        val i = Intent(this, ChatRoomActivity::class.java)
        i.putExtra("chatId", chat.chatId)
        i.putExtra("otherUserId", otherId)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.rvChatRooms.layoutManager = LinearLayoutManager(this)
        b.rvChatRooms.adapter = adapter

        b.btnBack.setOnClickListener { finish() }
    }

    override fun onStart() {
        super.onStart()
        val myId = auth.currentUser?.uid ?: return

        db.collection("chats")
            .whereArrayContains("participants", myId)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                val rooms = snap?.documents?.mapNotNull { d ->
                    d.toObject(ChatRoom::class.java)?.copy(chatId = d.id)
                } ?: emptyList()

                adapter.submitList(rooms)
                b.tvEmpty.visibility = if (rooms.isEmpty()) View.VISIBLE else View.GONE
            }
    }
}