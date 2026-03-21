package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.Burger
import com.example.zomatoclone.domain.repo.BurgerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BurgerViewModel @Inject constructor(
    private val burgerRepo: BurgerRepo) : ViewModel(){

    suspend fun getBurgerList () : List<Burger>{
        return withContext(Dispatchers.IO){
            burgerRepo.getBurgerList()
        }

    }

}