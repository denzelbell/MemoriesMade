package com.drawbytess.memoriesmade.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.drawbytess.memoriesmade.models.MemoriesModel

class DatabaseHandler(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MemoriesMade_Database"
        private const val TABLE_NAME = "MemoriesMadeTable"

        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_LONGITUDE = "longitude"
        private const val COLUMN_LATITUDE = "latitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_MEMORIES_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_IMAGE + " TEXT"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_LONGITUDE + " TEXT,"
                + COLUMN_LATITUDE + " TEXT)")

        db?.execSQL(CREATE_MEMORIES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addPlace(emp: MemoriesModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, emp.title)
        contentValues.put(COLUMN_IMAGE, emp.image)
        contentValues.put(COLUMN_DESCRIPTION, emp.description)
        contentValues.put(COLUMN_DATE, emp.date)
        contentValues.put(COLUMN_LOCATION, emp.location)
        contentValues.put(COLUMN_LONGITUDE, emp.longitude)
        contentValues.put(COLUMN_LATITUDE, emp.latitude)

        // Inserting Row
        val result = db.insert(TABLE_NAME, null, contentValues)

        db.close()
        return result
    }
}