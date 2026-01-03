package com.app.mindcare

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PilihDokterActivity : AppCompatActivity() {

    private lateinit var adapter: DoctorAdapter
    private lateinit var fullList: List<Doctor>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_dokter)

        val rvDoctor = findViewById<RecyclerView>(R.id.rvDoctor)
        val etSearch = findViewById<EditText>(R.id.etSearch)

        // Data Dokter (Gunakan Random.nextBoolean() tanpa kurung)
// --- DATA DOKTER YANG LEBIH BANYAK ---
        fullList = listOf(
            Doctor("dr. Sarah Amalia, Sp.KJ", "Psikiater", "Rp 150.000", R.drawable.dokter_sarah, true,
                "10 Tahun", "Universitas Indonesia (UI)", "RS Fatmawati, Jakarta", "STR-12345-67890-2024"),

            Doctor("Budi Santoso, M.Psi", "Psikolog Klinis", "Rp 100.000", R.drawable.dokter_budi, false,
                "5 Tahun", "Universitas Gadjah Mada (UGM)", "Pusat Psikologi Sleman", "STR-98765-43210-2023"),

            Doctor("dr. Andi Pratama, Sp.KJ", "Psikiater", "Rp 175.000", R.drawable.dokter_andi, true,
                "8 Tahun", "Universitas Airlangga (UNAIR)", "RS Jiwa Menur, Surabaya", "STR-15111-00223-1796"),

            Doctor("Nimas Ayu, M.Psi", "Psikolog Anak", "Rp 120.000", R.drawable.docter_nimas, true,
                "6 Tahun", "Universitas Padjadjaran (UNPAD)", "Klinik Harmoni, Bandung", "STR-11223-33445-2022"),

            Doctor("dr. Revanza, Sp.KJ", "Spesialis Adiksi", "Rp 200.000", R.drawable.dokter_revanza, true,
                "12 Tahun", "Universitas Diponegoro (UNDIP)", "RSUP Dr. Kariadi, Semarang", "STR-44556-77889-2024"),

            Doctor("Siti Aminah, M.Psi", "Konselor Pernikahan", "Rp 130.000", R.drawable.dokter_siti, false,
                "7 Tahun", "Universitas Sebelas Maret (UNS)", "Klinik Harapan, Solo", "STR-66778-99001-2023"),

            Doctor("dr. Lukman Hakim", "Psikiater Umum", "Rp 160.000", R.drawable.dokter_lukman, true,
                "9 Tahun", "Universitas Brawijaya (UB)", "RS Saiful Anwar, Malang", "STR-22334-55667-2024"),

            Doctor("Dewi Lestari, M.Psi", "Psikolog Pendidikan", "Rp 110.000", R.drawable.dokter_dewi, true,
                "4 Tahun", "Universitas Sumatera Utara (USU)", "Klinik Cerdas, Medan", "STR-88990-11223-2025"),

            Doctor("dr. Farhan Malik", "Spesialis Anxietas", "Rp 190.000", R.drawable.dokter_farhan, false,
                "15 Tahun", "Universitas Hasanuddin (UNHAS)", "RS Wahidin, Makassar", "STR-33445-66778-2024"),

            Doctor("Indah Permata, M.Psi", "Psikolog Trauma", "Rp 140.000", R.drawable.dokter_indah, true,
                "11 Tahun", "Universitas Udayana (UNUD)", "Klinik Jiwa Bali", "STR-55667-88990-2023")
        )

        adapter = DoctorAdapter(fullList)
        rvDoctor.layoutManager = LinearLayoutManager(this)
        rvDoctor.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filter(text: String) {
        val filteredList = fullList.filter {
            it.name.lowercase().contains(text.lowercase())
        }
        adapter.filterList(filteredList)
    }
}