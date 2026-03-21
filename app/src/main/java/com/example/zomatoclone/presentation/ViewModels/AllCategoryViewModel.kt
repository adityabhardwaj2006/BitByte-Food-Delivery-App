package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.AllCategoryItem
import com.example.zomatoclone.domain.repo.AllCategoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AllCategoryViewModel @Inject constructor(
    private val repo: AllCategoryRepo) : ViewModel(){

    suspend fun getAllCategory() : List<AllCategoryItem>{

        return withContext(Dispatchers.IO){
            repo.getAllCategory()
        }


    }


}