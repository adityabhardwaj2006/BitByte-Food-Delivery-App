package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.IceCream
import com.example.zomatoclone.domain.repo.IceCreamRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IceCreamViewModel @Inject constructor(
    private val repo : IceCreamRepo
) : ViewModel(){

    suspend fun getIceCreamList():List<IceCream>{
        return withContext(Dispatchers.IO){
            repo.getIceCreamList()
        }
    }

}