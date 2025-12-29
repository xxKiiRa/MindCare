package com.app.mindcare.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.mindcare.R
import org.json.JSONObject

private const val VIEW_TYPE_OUT = 1
private const val VIEW_TYPE_IN = 2

class MessageAdapter(private val myUid: MutableList<JSONObject>) : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        val msg = getItem(position)
        return if (msg.senderId == myUid) VIEW_TYPE_OUT else VIEW_TYPE_IN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_OUT) {
            val v = inflater.inflate(R.layout.item_message_out, parent, false)
            OutViewHolder(v)
        } else {
            val v = inflater.inflate(R.layout.item_message_in, parent, false)
            InViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = getItem(position)
        when (holder) {
            is OutViewHolder -> holder.bind(msg)
            is InViewHolder -> holder.bind(msg)
        }
    }

    class OutViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvMsg: TextView = view.findViewById(R.id.tvMsg)
        fun bind(m: ChatMessage) {
            tvMsg.text = m.text
        }
    }

    class InViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvMsg: TextView = view.findViewById(R.id.tvMsg)
        fun bind(m: ChatMessage) {
            tvMsg.text = m.text
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean = oldItem == newItem
    }
}
