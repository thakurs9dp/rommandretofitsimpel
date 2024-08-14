package com.s9dp.retrofitdemomvvm.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.s9dp.retrofitdemomvvm.R
import com.s9dp.retrofitdemomvvm.data.local.CountryDao
import com.s9dp.retrofitdemomvvm.data.local.CountryDatabase
import com.s9dp.retrofitdemomvvm.data.network.repo.CountryRepository
import com.s9dp.retrofitdemomvvm.data.network.services.ApiServices
import com.s9dp.retrofitdemomvvm.viewmodel.CountryViewModel
import com.s9dp.retrofitdemomvvm.viewmodel.CountryViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    //https://medium.com/@sandeepkella23/senior-android-developer-interview-questions-and-answers-lifecycle-9dce4f47aace
    //https://www.geeksforgeeks.org/top-50-android-interview-questions-answers-sde-i-to-sde-iii/


    private val apiService: ApiServices by lazy {
        Retrofit.Builder().baseUrl("https://restcountries.com/v3.1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiServices::class.java)
    }

    private val database: CountryDatabase by lazy {
        CountryDatabase.getDatabase(this)
    }

    // Step 2: Lazy initialization of the DAO
    private val countryDao: CountryDao by lazy {
        database.countryDao()
    }

    private val repository: CountryRepository by lazy {
        CountryRepository(apiService, countryDao)
    }

    private val viewModel: CountryViewModel by viewModels {
        CountryViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.countries.observe(this) { countries ->
            Log.d("TAG", "onCreate: ${countries[0]}")
            for (country in countries) {
                Log.d("TAG", "onCreate: ${country.name.common}")
                Log.d("TAG", "onCreate: ${country.currencies}")
                Log.d("TAG", "onCreate: ${country.capital}")
            }
        }

        getSingleCountriesData()
    }


    private fun getSingleCountriesData() {
        viewModel.getCountryData("Germany", "USD", "en").observe(this) { result ->
            val countriesByName = result["name"]
            val countriesByCurrency = result["currency"]
            val countriesByLanguage = result["language"]
            Log.d("TAG", countriesByName.toString())
            Log.d("TAG", countriesByCurrency.toString())
            Log.d("TAG", countriesByLanguage.toString())
        }


        viewModel.getCountryData2Method("Germany", "USD", "en").observe(this) {
            Log.d("TAG", "onCreate: ${it[0]}")
        }
    }
}