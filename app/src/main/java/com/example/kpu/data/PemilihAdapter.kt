package com.example.kpu.data

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kpu.R

class PemilihAdapter(
    private val listPemilih: MutableList<PemilihData>,
    private val database: DatabasePemilih,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<PemilihAdapter.ListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(dataPemilih: PemilihData)
    }

    class ListViewHolder(itemView: View,  private val  itemClickListener: OnItemClickListener, private val listPemilih: List<PemilihData>) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_pemilih)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_pemilih)
        val tvNik: TextView = itemView.findViewById(R.id.tv_nik_pemilih)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(listPemilih[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_data, parent, false)

        return ListViewHolder(view, itemClickListener, listPemilih)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listPemilih[position]

        holder.tvNama.text = item.nama
        holder.tvNik.text = item.nik
        val bitmap = BitmapFactory.decodeFile(item.photo) // Load image from file path
        holder.imgPhoto.setImageBitmap(bitmap)

    }

    override fun getItemCount(): Int = listPemilih.size
}