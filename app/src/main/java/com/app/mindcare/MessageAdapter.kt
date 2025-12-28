package com.app.mindcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class MessageAdapter(private val items: List<JSONObject>) : RecyclerView.Adapter<MessageAdapter.VH>() {
    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.tvMessageText)
        val time: TextView = view.findViewById(R.id.tvMessageTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val obj = items[position]
        holder.text.text = obj.optString("text")
        holder.time.text = obj.optString("time")
    }

    override fun getItemCount(): Int = items.size
}

