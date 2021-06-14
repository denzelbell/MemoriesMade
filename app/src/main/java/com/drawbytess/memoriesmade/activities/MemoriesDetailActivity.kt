package com.drawbytess.memoriesmade.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drawbytess.memoriesmade.R
import kotlinx.android.synthetic.main.activity_memories_detail.*

class MemoriesDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memories_detail)


        // Set up toolbar
        setSupportActionBar(toolbar_memories_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_memories_detail.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}