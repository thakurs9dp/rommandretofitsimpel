package com.s9dp.retrofitdemomvvm.data.network.repo

import androidx.lifecycle.LiveData
import com.s9dp.retrofitdemomvvm.data.local.CountryDao
import com.s9dp.retrofitdemomvvm.data.local.CountryEntity
import com.s9dp.retrofitdemomvvm.data.network.services.ApiServices
import com.s9dp.retrofitdemomvvm.model.CountriesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class CountryRepository(
    private val apiService: ApiServices, private val countryDao: CountryDao
) {

    suspend fun fetchCountries(): List<CountriesItem> {
        return apiService.getCountries()
    }

    suspend fun getCountryData(name: String, currency: String, language: String) = coroutineScope {

        val countriesByName = async { apiService.getCountryByName(name) }
        val countriesByCurrency = async { apiService.getCountryByCurrency(currency) }
        val countriesByLanguage = async { apiService.getCountryByLanguage(language) }

        // Await results
        val nameResult = countriesByName.await()
        val currencyResult = countriesByCurrency.await()
        val languageResult = countriesByLanguage.await()

        // Combine results or handle them as needed
        mapOf(
            "name" to nameResult, "currency" to currencyResult, "language" to languageResult
        )
    }


    fun getCountryByName(name: String): LiveData<List<CountryEntity>> {
        return countryDao.getCountryByName(name)
    }

    suspend fun fetchAndSaveCountryData(name: String, currency: String, language: String) {
        withContext(Dispatchers.IO) {
            try {
                val countriesByName = apiService.getCountryByName(name)
                val countriesByCurrency = apiService.getCountryByCurrency(currency)
                val countriesByLanguage = apiService.getCountryByLanguage(language)

                val allCountriesItem = apiService.getCountries()

                val countriesToSave = allCountriesItem.map {
                    CountryEntity(
                        name = it.name.common ?: "",
                        capital = it.capital.toString(),
                        region = it.region,
                        population = it.population
                    )
                }

                /*  // Convert the API response to CountryEntity and save it to the database
                  val countriesToSave =
                      countriesByName.map { it.toEntity() } + countriesByCurrency.map { it.toEntity() } + countriesByLanguage.map { it.toEntity() }*/

                countryDao.insertCountries(countriesToSave)
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }
}

