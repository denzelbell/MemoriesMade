package com.drawbytess.memoriesmade.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.drawbytess.memoriesmade.R
import com.drawbytess.memoriesmade.database.DatabaseHandler
import com.drawbytess.memoriesmade.models.MemoriesModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddLocation : AppCompatActivity(), View.OnClickListener {

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private var saveImageToInternalStorage : Uri? = null
    private var mLatitude : Double = 0.0
    private var mLongitude : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

        // Set up toolbar
        setSupportActionBar(toolbar_add_place)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_add_place.setNavigationOnClickListener {
            onBackPressed()
        }

        // Setup date dialog and update selected date in view.
        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateDateInView() // Updates the view once the date dialog is closed
            }
        updateDateInView() // Populations the current date by default

        et_date.setOnClickListener(this)

        // Setup add image button
        tv_add_image.setOnClickListener(this)

        // Setup save button
        btn_save.setOnClickListener (this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            /**
             * Set up DatePicker to popup when id et_date is selected
             */
            R.id.et_date -> {
                DatePickerDialog(
                        this@AddLocation,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            /**
             * Setup Dexter to request permissions popup when id tv_add_image is selected
             */
            R.id.tv_add_image -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems =
                    arrayOf("Select photo from Gallery",
                "Capture photo from camera")
                pictureDialog.setItems(
                    pictureDialogItems
                ) { dialog, which ->
                    when (which) {
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }
            R.id.btn_save -> {
                when {
                    et_title.text.isNullOrEmpty() -> {
                        Toast.makeText(this,
                            "Please enter a title",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    et_description.text.isNullOrEmpty() -> {
                        Toast.makeText(this,
                            "Please enter in a description",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    et_location.text.isNullOrEmpty() -> {
                        Toast.makeText(this,
                            "Please enter a location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    saveImageToInternalStorage == null -> {
                        Toast.makeText(this,
                        "Please select an image",
                        Toast.LENGTH_SHORT).show()
                    } else -> {
                        val memoriesModel = MemoriesModel(
                            0,
                            et_title.text.toString(),
                            saveImageToInternalStorage.toString(),
                            et_description.text.toString(),
                            et_date.text.toString(),
                            et_location.text.toString(),
                            mLatitude,
                            mLongitude
                        )
                    val dbhandler = DatabaseHandler(this)
                    val addPlace = dbhandler.addPlace(memoriesModel)

                    if(addPlace > 0){
                        Toast.makeText(
                            this,
                            "The happy place details are inserted successfully.",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY){
                if (data != null){
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)

                        saveImageToInternalStorage =
                            saveImageToInternalStorage(selectedImageBitmap)
                        Log.e(
                            "Saved Image : ", "Path :: $saveImageToInternalStorage")

                        iv_place_image.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddLocation,
                            "Failed to load the image from Gallery!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } else if (requestCode == CAMERA){
                val thumbNail: Bitmap = data!!.extras!!.get("data") as Bitmap

                saveImageToInternalStorage = saveImageToInternalStorage(thumbNail)
                Log.e(
                    "Saved Image : ", "Path :: $saveImageToInternalStorage")

                iv_place_image.setImageBitmap(thumbNail)
            }
        } else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("Cancelled", "Cancelled")
        }
    }

    /**
     * Puts selected date inside date picker text field
     */
    private fun updateDateInView(){
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        et_date.setText(sdf.format(cal.time).toString())
    }

    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    // Make report nullable (report!!) so that the Toast action will function properly.
                    if (report.areAllPermissionsGranted()) {
                        val galleryIntent = Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                        startActivityForResult(galleryIntent, GALLERY)
                    }
                }
                // If you user denies permission
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread()
            .check()
    }
    /**
     * Part of the Dexter permissions process.
     * Add permissions in manifest
     * Add "implementation 'com.karumi:dexter:6.2.2'" to gradle
     */
    private fun takePhotoFromCamera(){
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(
                    report: MultiplePermissionsReport) {

                    // Make report nullable (report!!) so that the Toast action will function properly.
                    if (report.areAllPermissionsGranted()) {
                        val galleryIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(galleryIntent, CAMERA)
                    }
                }
                // If you user denies permission
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread()
            .check()
    }

    // Informs the user why permission was needed
    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you denied permissions required to use this feature. It can be enabled in your Application Settings.")
            .setPositiveButton("GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){dialog, _ ->
                    dialog.dismiss()
                }.show()
    }

    /**
     * Place to save images to internal storage
     */
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri { // Uri is the location of the image being stored
        val wrapper = ContextWrapper(applicationContext)
        // File path
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE) // MODE_PRIVATE makes the file accessible only in the app sharing the same user ID.
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Stores the file from app to phone.
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }
        // Returns the Uri.
        return Uri.parse(file.absolutePath)
    }

    // Codes for permissions
    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "MemoriesMade" // Folder on phone to store images

    }

}