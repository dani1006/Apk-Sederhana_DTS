package com.example.kpu.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kpu.DetailActivity
import com.example.kpu.R
import com.example.kpu.data.DatabasePemilih
import com.example.kpu.data.PemilihAdapter
import com.example.kpu.data.PemilihData

class LihatData : Fragment(), PemilihAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PemilihAdapter
    private lateinit var emptyImageView: ImageView
    private lateinit var emptyText: TextView
    private lateinit var listPemilih: MutableList<PemilihData> // Initialize with an empty list

    override fun onItemClick(dataPemilih: PemilihData) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("capturedImage", dataPemilih.photo)
        intent.putExtra("nama", dataPemilih.nama)
        intent.putExtra("nik", dataPemilih.nik)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lihat_data, container, false)
        recyclerView = view.findViewById(R.id.rv_data_pemilih)
        emptyImageView = view.findViewById(R.id.img_empty)
        emptyText = view.findViewById(R.id.tv_empty)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Ambil data dari SQLite menggunakan DatabaseHandler
        val dbHandler = DatabasePemilih(requireContext())
        listPemilih = dbHandler.getAllScanResults().toMutableList()

        // Inisialisasi adapter dan atur RecyclerView
        adapter = PemilihAdapter(listPemilih, dbHandler, this)
        recyclerView.adapter = adapter

        updateVisibility()

        return view
    }

    private fun updateVisibility(){
        if (listPemilih.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyImageView.visibility = View.VISIBLE
            emptyText.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyImageView.visibility = View.GONE
            emptyText.visibility = View.GONE
        }

    }


}