package com.s9dp.retrofitdemomvvm.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val capital: String,
    val region: String,
    val population: Int,
)