package com.app.mindcare

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DoctorAdapter(private val list: List<Doctor>) : RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvDoctorName)
        val spec: TextView = view.findViewById(R.id.tvSpecialty)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val image: ImageView = view.findViewById(R.id.imgDoctor)
        val btnPilih: Button = view.findViewById(R.id.btnPilih)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dokter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = item.name
        holder.spec.text = item.specialty
        holder.price.text = item.price

        val imageCandidate = item.imageRes.toString()
        val ctx = holder.image.context
        val maybeInt = imageCandidate.toIntOrNull()
        val drawableId = if (maybeInt != null) {
            maybeInt
        } else {
            val id = ctx.resources.getIdentifier(imageCandidate, "drawable", ctx.packageName)
            if (id != 0) id else R.drawable.docter1
        }
        holder.image.setImageResource(drawableId)

        holder.btnPilih.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, PaymentActivity::class.java)

            intent.putExtra("NAMA_DOKTER", item.name)
            intent.putExtra("SPESIALIS", item.specialty)
            intent.putExtra("HARGA", item.price)
            intent.putExtra("GAMBAR", drawableId)

            context.startActivity(intent)
        }
    }

    override fun getItemCount() = list.size
}
