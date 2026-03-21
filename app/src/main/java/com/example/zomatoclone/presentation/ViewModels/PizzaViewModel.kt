package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.PizzaInfo
import com.example.zomatoclone.domain.repo.PizzaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PizzaViewModel @Inject constructor(
    private val pizzaRepo: PizzaRepo) : ViewModel(){

    suspend fun getPizzaInfo(): List<PizzaInfo> {
       return withContext(Dispatchers.IO){
           pizzaRepo.getPizzaData()
       }

    }


}