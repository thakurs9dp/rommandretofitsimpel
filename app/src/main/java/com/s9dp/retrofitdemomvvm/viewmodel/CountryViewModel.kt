package com.s9dp.retrofitdemomvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.s9dp.retrofitdemomvvm.data.local.CountryEntity
import com.s9dp.retrofitdemomvvm.data.network.repo.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountryViewModel(private val repository: CountryRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    val countries = liveData(Dispatchers.IO) {
        try {
            val response = repository.fetchCountries()
            emit(response)
        } catch (e: Exception) {
            Log.d("CountryViewModel", "Error fetching countries", e)
        }
    }


    fun getCountryData(name: String, currency: String, language: String) =
        liveData(Dispatchers.IO) {
            try {
                val result = repository.getCountryData(name, currency, language)
                emit(result)
            } catch (e: Exception) {
                Log.d("CountryViewModel", "Error fetching countries", e)
            }
        }


    fun getCountryData2Method(
        name: String, currency: String, language: String
    ): LiveData<List<CountryEntity>> {
        fetchCountryData(name, currency, language)
        return repository.getCountryByName(name)
    }


    private fun fetchCountryData(name: String, currency: String, language: String) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                repository.fetchAndSaveCountryData(name, currency, language)
                _loading.value = false
            } catch (e: Exception) {
                _error.value = "Failed to fetch data: ${e.message}"
                _loading.value = false
            }
        }
    }


}