package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.Rolls
import com.example.zomatoclone.domain.repo.RollsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RollsViewModel @Inject constructor(
    private val repo: RollsRepo
) : ViewModel(){


    suspend fun getRollsList() : List<Rolls>{
        return withContext(Dispatchers.IO){
            repo.getRollsList()
        }
    }


}