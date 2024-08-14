package com.s9dp.retrofitdemomvvm.data.network.services

import com.s9dp.retrofitdemomvvm.model.CountriesItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServices {

    @GET("all")
    suspend fun getCountries(): List<CountriesItem>

    @GET("name/{name}")
    suspend fun getCountryByName(@Path("name") name: String): List<CountriesItem.Name>

    @GET("currency/{currency}")
    suspend fun getCountryByCurrency(@Path("currency") currency: String): List<CountriesItem.Currencies>

    @GET("lang/{language}")
    suspend fun getCountryByLanguage(@Path("language") language: String): List<CountriesItem.Languages>

}