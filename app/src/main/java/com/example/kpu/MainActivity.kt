package com.example.kpu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.kpu.data.ListAdapter
import com.example.kpu.data.ListData
import com.example.kpu.fragment.FormEntry
import com.example.kpu.fragment.Informasi
import com.example.kpu.fragment.LihatData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lv_menu)
        val items = listOf(
            ListData(0, R.drawable.info,"Informasi"),
            ListData(1,R.drawable.form,"Form Entry"),
            ListData(2,R.drawable.lihat,"Lihat Data"),
            ListData(3,R.drawable.keluat,"Keluar")
        )
        supportActionBar?.show()

        val adapter = ListAdapter(this, items)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> showFragment(Informasi())
                1 -> showFragment(FormEntry())
                2 -> showFragment(LihatData())
                3 -> finish()
            }
        }
    }
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}