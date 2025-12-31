package com.app.mindcare.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.mindcare.R

private const val VIEW_TYPE_OUT = 1
private const val VIEW_TYPE_IN = 2

class MessageAdapter(private val myUid: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Diff callback for ChatMessage
    private val diffCallback = object : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean = oldItem == newItem
    }

    // AsyncListDiffer to compute diffs off the UI thread
    private val differ = AsyncListDiffer(this, diffCallback)

    // expose currentList similar to ListAdapter
    val currentList: List<ChatMessage>
        get() = differ.currentList

    // compat: submitList overload without callback
    fun submitList(list: List<ChatMessage>) {
        submitList(list) {}
    }

    // compat: submitList with commit callback
    fun submitList(list: List<ChatMessage>, commitCallback: () -> Unit) {
        // AsyncListDiffer supports a Runnable commit callback
        differ.submitList(ArrayList(list)) { commitCallback() }
    }

    override fun getItemViewType(position: Int): Int {
        val msg = differ.currentList[position]
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
        val msg = differ.currentList[position]
        when (holder) {
            is OutViewHolder -> holder.bind(msg)
            is InViewHolder -> holder.bind(msg)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

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
}
