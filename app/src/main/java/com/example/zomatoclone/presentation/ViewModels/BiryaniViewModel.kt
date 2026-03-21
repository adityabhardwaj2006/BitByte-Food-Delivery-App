package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.Biryani
import com.example.zomatoclone.domain.repo.BiryaniRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BiryaniViewModel @Inject constructor(
    private val repo : BiryaniRepo) : ViewModel(){

    suspend fun getBiryaniList () : List<Biryani>{
        return withContext(Dispatchers.IO) {
            repo.getBiryaniList()
        }
    }

}