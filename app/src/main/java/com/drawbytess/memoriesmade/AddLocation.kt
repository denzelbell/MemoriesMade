package com.drawbytess.memoriesmade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_location.*
import kotlinx.android.synthetic.main.activity_main.*

class AddLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

        // Set up toolbar
        setSupportActionBar(toolb_add_place)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolb_add_place.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}