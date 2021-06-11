package com.drawbytess.memoriesmade.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.drawbytess.memoriesmade.R
import com.drawbytess.memoriesmade.database.DatabaseHandler
import com.drawbytess.memoriesmade.models.MemoriesModel
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

        getMemoryListFromLocalDB()
    }

    private fun getMemoryListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getMemList: ArrayList<MemoriesModel> = dbHandler.getMemoriesList()

        if (getMemList.size > 0){
            for (i in getMemList){
                Log.e("Title", i.title)
                Log.e("Description", i.description)
                Log.e("Date", i.date)
                Log.e("Image", i.image)
            }
        }

    }
}