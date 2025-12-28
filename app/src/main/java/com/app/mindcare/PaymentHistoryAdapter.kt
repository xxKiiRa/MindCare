package com.app.mindcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class PaymentHistoryAdapter(private val items: List<JSONObject>) : RecyclerView.Adapter<PaymentHistoryAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val doctor: TextView = view.findViewById(R.id.tvHistDoctor)
        val detail: TextView = view.findViewById(R.id.tvHistDetail)
        val time: TextView = view.findViewById(R.id.tvHistTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val obj = items[position]
        holder.doctor.text = obj.optString("doctor")
        val ctx = holder.detail.context
        val formatted = ctx.getString(R.string.payment_detail_format, obj.optString("spec"), obj.optString("price"), obj.optString("method"))
        holder.detail.text = formatted
        holder.time.text = obj.optString("time")
    }

    override fun getItemCount(): Int = items.size
}
