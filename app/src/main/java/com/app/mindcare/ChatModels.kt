package com.app.mindcare.chat

import com.google.firebase.Timestamp

data class ChatRoom(
    val chatId: String = "",
    val patientId: String = "",
    val doctorId: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastSenderId: String = "",
    val updatedAt: Timestamp? = null
)

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val createdAt: Timestamp? = null
)
