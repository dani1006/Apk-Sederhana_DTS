package com.example.kpu

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.kpu.data.DatabasePemilih
import java.io.File

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.apply {
            title = "Detail"
            setDisplayHomeAsUpEnabled(true)
        }

        val nik = intent.getStringExtra("nik")

        val dbHandler = DatabasePemilih(this)
        val pemilihData = dbHandler.getPemilihByNik(nik) // Fetch the data from the database
        val imageView: ImageView = findViewById(R.id.ivGambar)
        val imagePath = intent.getStringExtra("capturedImage")
        val bitmap = getImageBitmapFromPath(imagePath)
        imageView.setImageBitmap(bitmap)


        // Set other views with pemilihData values
        val tvNama: TextView = findViewById(R.id.tvNama)
        val tvNik: TextView = findViewById(R.id.tvNIK)
        val tvNoHp: TextView = findViewById(R.id.tvNoHp)
        val tvJk: TextView = findViewById(R.id.tvJK)
        val tvTgl: TextView = findViewById(R.id.tvTgl)
        val tvAlamat: TextView = findViewById(R.id.tvAlamat)

        pemilihData?.let {
            tvNama.text = it.nama
            tvNik.text = it.nik
            tvNoHp.text = it.noHP
            tvJk.text = it.jk
            tvTgl.text = it.tgl
            tvAlamat.text = it.alamat
        }
    }
    private fun getImageBitmapFromPath(imagePath: String?): Bitmap? {
        val imageFile = File(imagePath)
        return BitmapFactory.decodeFile(imageFile.absolutePath)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed() // Aksi kembali
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}