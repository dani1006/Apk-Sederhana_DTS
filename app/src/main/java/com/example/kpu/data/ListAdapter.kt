package com.example.kpu.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.kpu.R

class ListAdapter(private val context: Context, private val items: List<ListData>) : BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(p0: Int): Any = items[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position) as ListData
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.card_list,
            parent,
            false
        )

        val itemTextView = view.findViewById<TextView>(R.id.tvTitle)
        itemTextView.text = item.title

        val icnView = view.findViewById<ImageView>(R.id.icnMenu)
        icnView.setImageResource(item.icon)

        return view
    }
}