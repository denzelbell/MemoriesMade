package com.drawbytess.memoriesmade

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class AddLocation : AppCompatActivity(), View.OnClickListener {

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

        // Set up toolbar
        setSupportActionBar(toolb_add_place)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolb_add_place.setNavigationOnClickListener {
            onBackPressed()
        }

        // Setup date dialog and update selected date in view.
        dateSetListener = DatePickerDialog.OnDateSetListener {
            view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView() // Updates the view once the date dialog is closed
        }
        et_date.setOnClickListener(this)

        // Setup add image button
        tv_add_image.setOnClickListener(this)

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
                        cal.get(Calendar.DAY_OF_MONTH))
                        .show()
            }
            /**
             * Setup Dexter to request permissions popup when id tv_add_image is selected
             */
            R.id.tv_add_image -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select photo from Gallery",
                "Capture photo from camera")
                pictureDialog.setItems(pictureDialogItems){
                    _, which ->
                    when(which){
                        0 -> choosePhotoFromGallery()
                        1 -> Toast.makeText(
                            this@AddLocation,
                            "Camera selection coming soon...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                pictureDialog.show()
            }
        }
    }

    /**
     * Part of the Dexter permissions process.
     * Add permissions in manifest
     * Add "implementation 'com.karumi:dexter:6.2.2'" to gradle
     */
    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ).withListener(object: MultiplePermissionsListener {
            override fun onPermissionsChecked(
                    report: MultiplePermissionsReport) {
                // Make report nullable (report!!) so that the Toast action will function properly.
                if (report!!.areAllPermissionsGranted()){
                        Toast.makeText(
                                this@AddLocation,
                                "Storage READ/WRITE permissions are granted. Now you can select an image from the GALLERY",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            // If you user denies permission
            override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }
    // Informs the user why permission was needed
    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage("It looks like you denied permissions required to use this feature. It can be enabled in your Application Settings.")
                .setPositiveButton("GO TO SETTINGS") {
                    _, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }.setNegativeButton("Cancel"){dialog, _ ->
                    dialog.dismiss()
                }.show()
    }

    /**
     * Places date selected in date picker in text field
     */
    private fun updateDateInView(){
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        et_date.setText(sdf.format(cal.time).toString())
    }


}