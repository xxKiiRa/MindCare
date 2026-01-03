package com.app.mindcare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PilihDokterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_dokter)

        val rvDoctor = findViewById<RecyclerView>(R.id.rvDoctor)


        val dataDokter = listOf(
            Doctor("dr. Sarah Amalia", "Psikiater", "Rp 150.000", R.drawable.docter1),
            Doctor("Budi Santoso, M.Psi", "Psikolog", "Rp 100.000", R.drawable.docter1),
            Doctor("dr. Andi Pratama", "Psikiater", "Rp 175.000", R.drawable.docter1)
        )

        rvDoctor.layoutManager = LinearLayoutManager(this)
        rvDoctor.adapter = DoctorAdapter(dataDokter)
    }
}