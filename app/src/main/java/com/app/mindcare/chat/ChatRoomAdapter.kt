package com.app.mindcare.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.mindcare.R
import com.app.mindcare.databinding.ItemChatRoomBinding

class ChatRoomAdapter(
    private val onClick: (ChatRoom) -> Unit
) : ListAdapter<ChatRoom, ChatRoomAdapter.VH>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemChatRoomBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(room: ChatRoom) {
            b.tvTitle.setText(R.string.action_chat)
            b.tvSubtitle.text = room.lastMessage.ifBlank { "Mulai chat..." }
            b.root.setOnClickListener { onClick(room) }
        }
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<ChatRoom>() {
            override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom) = oldItem.chatId == newItem.chatId
            override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom) = oldItem == newItem
        }
    }
}