package com.drawbytess.memoriesmade.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.recyclerview.widget.LinearLayoutManager
import com.drawbytess.memoriesmade.R
import com.drawbytess.memoriesmade.adapters.MemoriesMadeAdapter
import com.drawbytess.memoriesmade.database.DatabaseHandler
import com.drawbytess.memoriesmade.models.MemoriesModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)

        fabAddLoc.setOnClickListener {
            // TODO: (3: Need to find work around for automatic update for recyclerview.)
            val intent = Intent(this@MainActivity,
                    AddLocationActivity::class.java)

            startActivityForResult(intent, ADD_PLACE_REQUEST_CODE)

        }

        getMemoryListFromLocalDB()
    }

    // TODO: (2: onActivityResult is no longer used. Find work around.)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ADD_PLACE_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                getMemoryListFromLocalDB()
            }else{
                Log.e("Activity", "Cancelled or Back pressed")
            }
        }
    }


   // TODO: (1: Fix automatic update that is suppose to occur once a memory is added.)

    private fun getMemoryListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getMemList = dbHandler.getMemoriesList()

        if (getMemList.size > 0){
            rv_memories_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE

            setupMemoryRecyclerView(getMemList)
        } else {
            rv_memories_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }

    // Adds items to recyclerView
    private fun setupMemoryRecyclerView(
        memoryList: ArrayList<MemoriesModel>){

        rv_memories_list.layoutManager = LinearLayoutManager(this)
        rv_memories_list.setHasFixedSize(true)

        val placesAdapter = MemoriesMadeAdapter(this, memoryList)
        rv_memories_list.adapter = placesAdapter
    }

    companion object {
        private const val ADD_PLACE_REQUEST_CODE = 1
    }
}