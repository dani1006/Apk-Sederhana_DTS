package com.example.kpu.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PemilihData(
    val nik: String,
    val nama: String,
    val noHP: String,
    val jk: String,
    val tgl: String,
    val alamat: String,
    val photo: String
)