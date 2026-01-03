package com.app.mindcare

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

// PASTIKAN: Tidak ada import com.google.androidbrowserhelper di sini!

class DoctorAdapter(private var list: List<Doctor>) : RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvDoctorName)
        val spec: TextView = view.findViewById(R.id.tvSpecialty)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val image: ImageView = view.findViewById(R.id.imgDoctor)
        val btnPilih: Button = view.findViewById(R.id.btnPilih)
        val onlineIndicator: View = view.findViewById(R.id.viewOnlineIndicator)
    }

    fun filterList(newList: List<Doctor>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dokter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val context = holder.itemView.context

        // 1. Tampilkan Data Dasar
        holder.name.text = item.name
        holder.spec.text = item.specialty
        holder.price.text = item.price

        // 2. Status Online (Visual)
        holder.onlineIndicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE

        // Tambahan: Ubah tampilan tombol jika offline
        if (item.isOnline) {
            holder.btnPilih.alpha = 1.0f
            holder.btnPilih.text = "Pilih"
        } else {
            holder.btnPilih.alpha = 0.5f
            holder.btnPilih.text = "Offline"
        }

        // 3. Pasang Gambar
        try {
            holder.image.setImageResource(item.imageRes)
        } catch (e: Exception) {
            holder.image.setImageResource(R.drawable.docter_nimas) // Gambar default
        }

        holder.itemView.setOnClickListener {
            // TAMBAHKAN BARIS INI: ambil context dari view yang diklik
            val context = holder.itemView.context

            val intent = Intent(context, DetailDokterActivity::class.java)

            intent.putExtra("NAMA", item.name)
            intent.putExtra("SPESIALIS", item.specialty)
            intent.putExtra("HARGA", item.price)
            intent.putExtra("GAMBAR", item.imageRes)
            intent.putExtra("ONLINE", item.isOnline)
            intent.putExtra("PENGALAMAN", item.pengalaman)
            intent.putExtra("ALUMNUS", item.alumnus)
            intent.putExtra("PRAKTIK", item.praktikDi)
            intent.putExtra("STR", item.nomorStr)

            context.startActivity(intent)
        }

        // 5. KLIK TOMBOL PILIH (Langsung ke Payment)
        holder.btnPilih.setOnClickListener {
            if (item.isOnline) {
                // Jika Online -> Lanjut Bayar
                val intent = Intent(context, PaymentActivity::class.java)
                intent.putExtra("PAY_NAMA", item.name)
                intent.putExtra("PAY_SPEC", item.specialty)
                intent.putExtra("PAY_HARGA", item.price)
                intent.putExtra("PAY_GAMBAR", item.imageRes)
                context.startActivity(intent)
            } else {
                // Jika Offline -> Tampilkan Peringatan
                Toast.makeText(
                    context,
                    "Dokter ${item.name} sedang tidak aktif. Silakan pilih dokter lain.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount() = list.size
}