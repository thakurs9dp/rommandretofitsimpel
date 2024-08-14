package com.s9dp.retrofitdemomvvm.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [CountryEntity::class], version = 1)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao

    companion object {
        @Volatile
        private var INSTANCE: CountryDatabase? = null

        fun getDatabase(context: Context): CountryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, CountryDatabase::class.java, "country_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
