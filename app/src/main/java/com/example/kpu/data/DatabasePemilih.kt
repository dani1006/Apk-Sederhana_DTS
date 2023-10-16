package com.example.kpu.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabasePemilih(context:Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "PemilihDB"
        private const val TABLE_PEMILIH = "DataPemilih"
        private const val KEY_NIK = "nik" //nama kolom
        private const val KEY_NAMA = "nama"
        private const val KEY_NOHP = "noHp"
        private const val KEY_JK = "jk"
        private const val KEY_TANGGAL = "tgl"
        private const val KEY_ALAMAT = "alamat"
        private const val KEY_IMAGE_PATH = "image_path"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_PEMILIH " +
                "($KEY_NIK INTEGER PRIMARY KEY, $KEY_NAMA TEXT, $KEY_NOHP INT, $KEY_JK TEXT, $KEY_TANGGAL TEXT, $KEY_ALAMAT TEXT,$KEY_IMAGE_PATH TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PEMILIH")
        onCreate(db)
    }

    fun addDataPemilih(dataPemilih: PemilihData): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NIK, dataPemilih.nik)
        values.put(KEY_NAMA, dataPemilih.nama)
        values.put(KEY_NOHP, dataPemilih.noHP)
        values.put(KEY_JK, dataPemilih.jk)
        values.put(KEY_TANGGAL, dataPemilih.tgl)
        values.put(KEY_ALAMAT, dataPemilih.alamat)
        values.put(KEY_IMAGE_PATH, dataPemilih.photo)

        val id = db.insert(TABLE_PEMILIH, null, values) // Get the inserted id
        db.close() // Close the database connection
        return id // Return the inserted id
    }

    fun getAllScanResults(): List<PemilihData> {
        val scanResultsList = mutableListOf<PemilihData>()
        val selectQuery = "SELECT * FROM $TABLE_PEMILIH"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor != null && cursor.moveToFirst()) {
            val nikIndex = cursor.getColumnIndex(KEY_NIK)
            val namaIndex = cursor.getColumnIndex(KEY_NAMA)
            val noHPIndex = cursor.getColumnIndex(KEY_NOHP)
            val jkIndex = cursor.getColumnIndex(KEY_JK)
            val tglIndex = cursor.getColumnIndex(KEY_TANGGAL)
            val alamatIndex = cursor.getColumnIndex(KEY_ALAMAT)
            val imagePathIndex = cursor.getColumnIndex(KEY_IMAGE_PATH)

            do {
                val nik = cursor.getString(nikIndex)
                val nama = cursor.getString(namaIndex)
                val noHp = cursor.getString(noHPIndex)
                val jk = cursor.getString(jkIndex)
                val tgl = cursor.getString(tglIndex)
                val alamat = cursor.getString(alamatIndex)
                val imagePath = cursor.getString(imagePathIndex)
//                val imagePath = cursor.getBlob(imagePathIndex)
                val dataPemilih = PemilihData(nik,nama, noHp, jk, tgl, alamat, imagePath)
                scanResultsList.add(dataPemilih)
            } while (cursor.moveToNext())
        }

        cursor?.close()
        return scanResultsList
    }

    fun getPemilihByNik(nik: String?): PemilihData? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_PEMILIH WHERE $KEY_NIK = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(nik))
        var pemilihData: PemilihData? = null

        if (cursor != null && cursor.moveToFirst()) {
            val nikIndex = cursor.getColumnIndex(KEY_NIK)
            val namaIndex = cursor.getColumnIndex(KEY_NAMA)
            val noHPIndex = cursor.getColumnIndex(KEY_NOHP)
            val jkIndex = cursor.getColumnIndex(KEY_JK)
            val tglIndex = cursor.getColumnIndex(KEY_TANGGAL)
            val alamatIndex = cursor.getColumnIndex(KEY_ALAMAT)
            val imagePathIndex = cursor.getColumnIndex(KEY_IMAGE_PATH)

            do {
                val nik = cursor.getString(nikIndex)
                val nama = cursor.getString(namaIndex)
                val noHp = cursor.getString(noHPIndex)
                val jk = cursor.getString(jkIndex)
                val tgl = cursor.getString(tglIndex)
                val alamat = cursor.getString(alamatIndex)
//                val imagePath = cursor.getBlob(imagePathIndex)
                val imagePath = cursor.getString(imagePathIndex)
//                val bitmap = BitmapFactory.decodeByteArray(imagePath, 0, imagePath.size)
                pemilihData = PemilihData(nik,nama, noHp, jk, tgl, alamat, imagePath )
            } while (cursor.moveToNext())
        }

        cursor?.close()
        return pemilihData
    }

    fun checkNIKExists(nik: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_PEMILIH WHERE $KEY_NIK = ?"
        val cursor = db.rawQuery(query, arrayOf(nik))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

}