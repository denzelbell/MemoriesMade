package com.drawbytess.memoriesmade.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drawbytess.memoriesmade.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolb_main)

        fabAddLoc.setOnClickListener {
            val intent = Intent(
                    this@MainActivity,
                    AddLocation::class.java)
            startActivity(intent)
        }
    }
}