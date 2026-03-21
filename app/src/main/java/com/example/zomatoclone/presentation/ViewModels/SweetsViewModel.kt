package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.Sweets
import com.example.zomatoclone.domain.repo.SweetsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SweetsViewModel @Inject constructor(
    private val repo : SweetsRepo
) : ViewModel(){

    suspend fun getSweetList () : List<Sweets>{
        return withContext(Dispatchers.IO){
            repo.getSweetsList()
        }
    }

}