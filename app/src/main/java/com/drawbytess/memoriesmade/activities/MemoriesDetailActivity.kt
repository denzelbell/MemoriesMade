package com.drawbytess.memoriesmade.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drawbytess.memoriesmade.R
import com.drawbytess.memoriesmade.models.MemoriesModel
import kotlinx.android.synthetic.main.activity_memories_detail.*

class MemoriesDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memories_detail)

        var memoryPlaceDetailModel : MemoriesModel? = null

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            memoryPlaceDetailModel =
                intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS) as MemoriesModel
        }

        if (memoryPlaceDetailModel != null) {
            setSupportActionBar(toolbar_memories_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = memoryPlaceDetailModel.title

            toolbar_memories_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(memoryPlaceDetailModel.image))
            tv_description.text = memoryPlaceDetailModel.description
            tv_location.text = memoryPlaceDetailModel.location
        }
    }
}