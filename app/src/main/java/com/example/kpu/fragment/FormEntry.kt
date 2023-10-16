package com.example.kpu.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.util.Calendar
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kpu.R
import com.example.kpu.data.DatabasePemilih
import com.example.kpu.data.PemilihData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.*


class FormEntry : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var etNIK: EditText
    private lateinit var etNama: EditText
    private lateinit var etNoHp: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var etTanggal: EditText
    private lateinit var etAlamat: EditText
    private lateinit var btnSubmit: Button
    private lateinit var databasePemilih: DatabasePemilih
    private lateinit var imgView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_form_entry, container, false)

        etNIK = view.findViewById(R.id.etNIK)
        etNama = view.findViewById(R.id.etNama)
        etNoHp = view.findViewById(R.id.etNoHp)
        radioGroup = view.findViewById(R.id.radioGroup)
        etTanggal = view.findViewById(R.id.etTanggal)
        etAlamat = view.findViewById(R.id.etAlamat)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        val btnGaleri = view.findViewById<Button>(R.id.btnGaleri)
        val btnCamera = view.findViewById<Button>(R.id.btnCamera)
        val btnLokasi = view.findViewById<Button>(R.id.btnLokasi)
        imgView = view.findViewById(R.id.imgView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        etNIK.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                checkAndNavigateToDetailPage(etNIK.text.toString())
            }
        }

        btnLokasi.setOnClickListener {
            requestLocation()
        }

        etTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        btnGaleri.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted, launch the camera
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST)
            } else {
                // Request camera permission
                requestCameraPermission()
            }
        }



        databasePemilih = DatabasePemilih(requireContext())

        btnSubmit.setOnClickListener {
            saveData()
        }

        return view
    }


    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }




    private fun checkAndNavigateToDetailPage(nik: String) {
        val databaseHelper = DatabasePemilih(requireContext())
        val existsInDatabase = databaseHelper.checkNIKExists(nik)

        if (existsInDatabase) {
            showAlertDialog()
        } else {
            //tidak ada apa-apa
        }
    }


    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setTitle("Peringatan")
        alertDialogBuilder.setMessage("NIK sudah terdaftar")
        alertDialogBuilder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->

            etNIK.setText("")
            etNIK.requestFocus()
            dialog.dismiss() // Close the dialog
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun requestLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val geocoder = Geocoder(requireContext())
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            val address = addresses[0]
                            val addressString = address.getAddressLine(0) ?: "Address not found"
                            etAlamat.setText(addressString)
                        } else {
                            etAlamat.setText("Address not found")
                        }
                    }
                } else {
                    etAlamat.setText("Location not available")
                }
            }

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                // Permission denied
                Toast.makeText(
                    requireContext(), "Location permission denied", Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch the camera
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST)
            } else {
                // Permission denied, show a message to the user or handle it accordingly
            }
        }
    }


    private fun getLocation() {

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }


    private fun saveData() {
        val alamat = etAlamat.text.toString()
        val nik = etNIK.text.toString()
        val nama = etNama.text.toString()
        val noHp = etNoHp.text.toString()
        val radioButtonId = radioGroup.checkedRadioButtonId
        val jk = if (radioButtonId == R.id.rbLakiLaki) "Laki-Laki" else "Perempuan"
        val tanggal = etTanggal.text.toString()


        val capturedImageBitmap = getImageBitmapFromImageView(imgView)
        val imagePath = saveImageToInternalStorage(capturedImageBitmap)

        val pemilihData = PemilihData(nik, nama, noHp, jk, tanggal, alamat, imagePath)


        val insertedId = databasePemilih.addDataPemilih(pemilihData)

        if (insertedId != -1L) {
            // Data inserted successfully
            Toast.makeText(requireContext(), "Data pemilih berhasil diinput", Toast.LENGTH_SHORT).show()
        } else {
            // Failed to insert data
            Toast.makeText(requireContext(), "Gagal mengunput data", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri = data?.data
                    if (selectedImageUri != null) {
                        imgView.setImageURI(selectedImageUri)
                    }
                }
                CAMERA_REQUEST -> {
                    val capturedImage = data?.extras?.get("data") as Bitmap?
                    if (capturedImage != null) {
                        imgView.setImageBitmap(capturedImage)
                        // Here you can save the capturedImage to your PemilihData object
                    }
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    "%d-%02d-%02d", selectedDay, selectedMonth + 1, selectedYear
                )
                etTanggal.setText(formattedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }


    private fun getImageBitmapFromImageView(imageView: ImageView): Bitmap? {
        return (imageView.drawable as? BitmapDrawable)?.bitmap
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap?): String {
        val wrapper = ContextWrapper(context)
        val file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        val imageFile = File(file, "image_${System.currentTimeMillis()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(imageFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return imageFile.absolutePath
    }


    override fun onDestroyView() {
        super.onDestroyView()
        databasePemilih.close()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val CAMERA_REQUEST = 2
        private const val LOCATION_PERMISSION_REQUEST_CODE = 3
        private const val CAMERA_PERMISSION_REQUEST_CODE = 4

    }

}