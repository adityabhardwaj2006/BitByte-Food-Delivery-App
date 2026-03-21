package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.ChineseFood
import com.example.zomatoclone.domain.repo.ChineseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChineseViewModel @Inject constructor(
    private val chineseRepo: ChineseRepo) : ViewModel(){

    suspend fun getChineseFood(): List<ChineseFood> {
        return withContext(Dispatchers.IO){
            chineseRepo.getChineseFood()
        }


    }

}
