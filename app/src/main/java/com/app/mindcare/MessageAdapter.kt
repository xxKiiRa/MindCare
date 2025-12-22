package com.app.mindcare.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.mindcare.databinding.ItemMessageInBinding
import com.app.mindcare.databinding.ItemMessageOutBinding

class MessageAdapter(private val myUid: String) : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(diff) {

    private val TYPE_OUT = 1
    private val TYPE_IN = 2

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == myUid) TYPE_OUT else TYPE_IN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_OUT) {
            val b = ItemMessageOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            OutVH(b)
        } else {
            val b = ItemMessageInBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            InVH(b)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = getItem(position)
        when (holder) {
            is OutVH -> holder.bind(msg)
            is InVH -> holder.bind(msg)
        }
    }

    inner class OutVH(private val b: ItemMessageOutBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: ChatMessage) {
            b.tvMsg.text = m.text
        }
    }

    inner class InVH(private val b: ItemMessageInBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: ChatMessage) {
            b.tvMsg.text = m.text
        }
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) = oldItem == newItem
        }
    }
}
