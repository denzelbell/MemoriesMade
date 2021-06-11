package com.drawbytess.memoriesmade.database

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.drawbytess.memoriesmade.models.MemoriesModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MemoriesMade_Database"
        private const val TABLE_NAME_MEMORY = "MemoriesMadeTable"

        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_MEMORIES_TABLE = ("CREATE TABLE " + TABLE_NAME_MEMORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")

        db?.execSQL(CREATE_MEMORIES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_MEMORY")
        onCreate(db)
    }

    fun addMemPlace(memorialPlace: MemoriesModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, memorialPlace.title)
        contentValues.put(KEY_IMAGE, memorialPlace.image)
        contentValues.put(KEY_DESCRIPTION, memorialPlace.description)
        contentValues.put(KEY_DATE, memorialPlace.date)
        contentValues.put(KEY_LOCATION, memorialPlace.location)
        contentValues.put(KEY_LONGITUDE, memorialPlace.longitude)
        contentValues.put(KEY_LATITUDE, memorialPlace.latitude)

        // Inserting Row
        val result = db.insert(TABLE_NAME_MEMORY, null, contentValues)

        db.close()
        return result
    }
    fun getMemoriesList(): ArrayList<MemoriesModel>{
        val memoriesList = ArrayList<MemoriesModel>()
        val selectQuery = "SELECT * FROM $TABLE_NAME_MEMORY"
        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()){
                do {
                    val place = MemoriesModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE))
                    )
                    memoriesList.add(place)

                } while (cursor.moveToNext())
            }
            cursor.close()

        } catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return memoriesList
    }
}